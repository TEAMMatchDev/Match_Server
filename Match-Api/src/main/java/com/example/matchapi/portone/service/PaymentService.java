package com.example.matchapi.portone.service;

import com.example.matchapi.donation.service.DonationHistoryService;
import com.example.matchapi.order.convertor.OrderConvertor;
import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchapi.portone.dto.PaymentReq;
import com.example.matchcommon.annotation.RedissonLock;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.properties.PortOneProperties;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.UserCard;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.project.adaptor.ProjectAdaptor;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.redis.entity.OrderRequest;
import com.example.matchdomain.redis.repository.OrderRequestRepository;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.repository.UserRepository;
import com.example.matchinfrastructure.pay.portone.client.PortOneFeignClient;
import com.example.matchinfrastructure.pay.portone.convertor.PortOneConvertor;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillPayResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneResponse;
import com.example.matchinfrastructure.pay.portone.service.PortOneAuthService;
import com.example.matchinfrastructure.pay.portone.service.PortOneService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import static com.example.matchcommon.constants.MatchStatic.ONE_TIME;
import static com.example.matchdomain.order.exception.PortOneAuthErrorCode.*;
import static com.example.matchdomain.project.exception.ProjectOneTimeErrorCode.PROJECT_NOT_EXIST;
import static com.example.matchdomain.user.exception.UserLoginErrorCode.NOT_EXIST_USER;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final OrderRequestRepository orderRequestRepository;
    private final UserRepository userRepository;
    private final OrderHelper orderHelper;
    private final DonationUserRepository donationUserRepository;
    private final OrderConvertor orderConvertor;
    private final IamportClient iamportClient;
    private final ProjectAdaptor projectAdaptor;
    private final DonationHistoryService donationHistoryService;
    private final PortOneProperties portOneProperties;
    private final PortOneConvertor portOneConvertor;
    private final PortOneFeignClient portOneFeignClient;
    private final PortOneAuthService portOneAuthService;


    @Autowired
    public PaymentService(PortOneProperties portOneProperties,
                          OrderRequestRepository orderRequestRepository,
                          UserRepository userRepository,
                          OrderHelper orderHelper,
                          DonationUserRepository donationUserRepository,
                          OrderConvertor orderConvertor, ProjectAdaptor projectAdaptor, DonationHistoryService donationHistoryService, PortOneConvertor portOneConvertor, PortOneFeignClient portOneFeignClient, PortOneAuthService portOneAuthService) {
        this.portOneProperties = portOneProperties;
        this.orderRequestRepository = orderRequestRepository;
        this.userRepository = userRepository;
        this.orderHelper = orderHelper;
        this.donationUserRepository = donationUserRepository;
        this.orderConvertor = orderConvertor;
        this.iamportClient = new IamportClient(portOneProperties.getKey(), portOneProperties.getSecret());
        this.projectAdaptor = projectAdaptor;
        this.donationHistoryService = donationHistoryService;
        this.portOneConvertor = portOneConvertor;
        this.portOneFeignClient = portOneFeignClient;
        this.portOneAuthService = portOneAuthService;
    }

    @RedissonLock(LockName = "결제-검증", key = "#validatePayment.orderId")
    public OrderRes.CompleteDonation checkPayment(PaymentReq.ValidatePayment validatePayment){
        try {
            OrderRequest orderRequest = orderRequestRepository.findById(validatePayment.getOrderId()).orElseThrow(()->new BadRequestException(NOT_EXIST_ORDER_ID));

            log.info(orderRequest.getOrderId());

            IamportResponse<Payment> payment = iamportClient.paymentByImpUid(validatePayment.getImpUid());

            if(payment.getResponse().getAmount().intValue()!=validatePayment.getAmount()) throw new BadRequestException(FAILED_ERROR_AUTH_AMOUNT);

            User user = userRepository.findByIdAndStatus(Long.valueOf(orderRequest.getUserId()), Status.ACTIVE).orElseThrow(()->new BadRequestException(NOT_EXIST_USER));

            Project project = projectAdaptor.findByProjectId(Long.valueOf(orderRequest.getProjectId())).orElseThrow(()->new BadRequestException(PROJECT_NOT_EXIST));

            saveDonationUser(user, validatePayment, project);

            orderRequestRepository.deleteById(validatePayment.getOrderId());

            return orderConvertor.convertToCompleteDonation(user.getName(), project, (long) validatePayment.getAmount());
        } catch (BadRequestException | IamportResponseException | IOException e) {
            try {
                refundPayment(validatePayment.getImpUid());
            } catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
            throw new BadRequestException(FAILED_ERROR_AUTH);
        }
    }

    private CancelData createCancelData(IamportResponse<Payment> response, int refundAmount) {
        if (refundAmount == 0) { //전액 환불일 경우
            return new CancelData(response.getResponse().getImpUid(), true);
        }
        return new CancelData(response.getResponse().getImpUid(), true, new BigDecimal(refundAmount));
    }

    public void refundPayment(String impUid) {
        try {
            iamportClient.cancelPaymentByImpUid(new CancelData(impUid, true));
        } catch (IamportResponseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveDonationUser(User user, PaymentReq.ValidatePayment validatePayment, Project project) {
        OrderRes.CreateInherenceDto createInherenceDto = orderHelper.createInherence(user);
        DonationUser donationUser = donationUserRepository.save(orderConvertor.convertToDonationUserPortone(user.getId(), validatePayment, project.getId(), createInherenceDto));
        donationHistoryService.oneTimeDonationHistory(donationUser.getId());
    }

    public PortOneResponse<PortOneBillPayResponse> payBillKey(UserCard card, Long amount, String projectName, String type) {
        String orderId = orderHelper.createOrderId(type);
        String accessToken = portOneAuthService.getToken();
        return portOneFeignClient.payWithBillKey(accessToken, portOneConvertor.convertPayWithBillKey(card.getBid(), orderId, amount, projectName, card.getCustomerId()));
    }
}

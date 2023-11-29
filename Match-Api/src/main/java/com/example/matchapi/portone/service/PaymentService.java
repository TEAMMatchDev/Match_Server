package com.example.matchapi.portone.service;

import com.example.matchapi.common.security.JwtService;
import com.example.matchapi.donation.service.DonationHistoryService;
import com.example.matchapi.order.converter.OrderConverter;
import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchapi.portone.dto.PaymentCommand;
import com.example.matchapi.portone.dto.PaymentReq;
import com.example.matchapi.user.service.AligoService;
import com.example.matchcommon.annotation.PaymentIntercept;
import com.example.matchcommon.annotation.RedissonLock;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.BaseException;
import com.example.matchcommon.properties.PortOneProperties;
import com.example.matchdomain.donation.adaptor.DonationAdaptor;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.UserCard;
import com.example.matchdomain.project.adaptor.ProjectAdaptor;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.redis.adaptor.OrderAdaptor;
import com.example.matchdomain.redis.entity.OrderRequest;
import com.example.matchdomain.user.adaptor.UserAdaptor;
import com.example.matchdomain.user.entity.User;
import com.example.matchinfrastructure.aligo.converter.AligoConverter;
import com.example.matchinfrastructure.aligo.dto.AlimType;
import com.example.matchinfrastructure.pay.portone.client.PortOneFeignClient;
import com.example.matchinfrastructure.pay.portone.converter.PortOneConverter;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillPayResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneWebhook;
import com.example.matchinfrastructure.pay.portone.service.PortOneAuthService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static com.example.matchcommon.constants.MatchStatic.CANCEL_STATUS;
import static com.example.matchdomain.order.exception.PortOneAuthErrorCode.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final OrderAdaptor orderAdaptor;
    private final OrderHelper orderHelper;
    private final OrderConverter orderConverter;
    private final DonationHistoryService donationHistoryService;
    private final PortOneProperties portOneProperties;
    private final PortOneConverter portOneConverter;
    private final PortOneFeignClient portOneFeignClient;
    private final PortOneAuthService portOneAuthService;
    private final DonationAdaptor donationAdaptor;
    private final ProjectAdaptor projectAdaptor;
    private final UserAdaptor userAdaptor;
    private final AligoService aligoService;
    private final AligoConverter aligoConverter;
    private final JwtService jwtService;

    private IamportClient iamportClient;

    @PostConstruct
    private void init() {
        this.iamportClient = new IamportClient(portOneProperties.getKey(), portOneProperties.getSecret());
    }

    @RedissonLock(LockName = "결제-검증", key = "#paymentValidation.validatePayment.orderId")
    public OrderRes.CompleteDonation checkPayment(PaymentCommand.PaymentValidation paymentValidation){
        try {
            IamportResponse<Payment> payment = iamportClient.paymentByImpUid(paymentValidation.getValidatePayment().getImpUid());

            validatePayments(payment, paymentValidation.getValidatePayment(), paymentValidation.getOrderRequest());

            DonationUser donationUser = saveDonationUser(paymentValidation.getUser(), paymentValidation.getProject(), payment.getResponse());

            orderAdaptor.deleteById(paymentValidation.getValidatePayment().getOrderId());

            aligoService.sendAlimTalk(jwtService.createToken(1L), AlimType.PAYMENT, aligoConverter.convertToAlimTalkPayment(donationUser.getId(), paymentValidation.getUser().getName(), paymentValidation.getUser().getPhoneNumber()));

            return orderConverter.convertToCompleteDonation(paymentValidation.getUser().getName(), paymentValidation.getProject(), (long) paymentValidation.getValidatePayment().getAmount());
        } catch (IamportResponseException | IOException e) {
            throw new BadRequestException(FAILED_ERROR_AUTH);
        }
    }


    private void validatePayments(IamportResponse<Payment> payment, PaymentReq.ValidatePayment validatePayment, OrderRequest orderRequest) {
        checkCancelPayment(payment.getResponse().getStatus());

        checkExistsImpUid(validatePayment.getImpUid());

        checkPaymentAmount(payment.getResponse().getAmount().intValue(), orderRequest.getAmount());

        checkOrderId(payment.getResponse().getMerchantUid(), validatePayment.getOrderId());
    }

    private void checkOrderId(String merchantUid, String orderId) {
        if(!merchantUid.equals(orderId)) throw new BadRequestException(NOT_CORRECT_ORDER_ID);
    }

    private void checkPaymentAmount(int paymentAmount, int amount) {
        if(paymentAmount!=amount) throw new BadRequestException(FAILED_ERROR_AUTH_AMOUNT);
    }

    private void checkExistsImpUid(String impUid) {
        if(donationAdaptor.existsByImpId(impUid)) throw new BadRequestException(EXIST_IMP_UID);
    }

    private void checkCancelPayment(String status) {
        if(status.equals(CANCEL_STATUS)) throw new BadRequestException(EXIST_CANCEL_STATUS);
    }

    @PaymentIntercept(key = "#payment.imp_uid")
    public DonationUser saveDonationUser(User user, Project project, Payment payment) {
        OrderRes.CreateInherenceDto createInherenceDto = orderHelper.createInherence(user);
        DonationUser donationUser = donationAdaptor.save(orderConverter.convertToDonationUserPortone(user.getId(), payment, project.getId(), createInherenceDto));
        donationHistoryService.oneTimeDonationHistory(donationUser.getId());
        return donationUser;
    }

    public PortOneResponse<PortOneBillPayResponse> payBillKey(UserCard card, Long amount, String projectName, String orderId) {
        String accessToken = portOneAuthService.getToken();

        PortOneResponse<PortOneBillPayResponse> portOneResponse = portOneFeignClient.payWithBillKey(accessToken, portOneConverter.convertPayWithBillKey(card.getBid(), orderId, amount, projectName, card.getCustomerId()));

        if (portOneResponse.getCode()!=0){
            if (portOneResponse.getCode() != 0) {
                throw new BaseException(BAD_REQUEST, false, "PORT_ONE_BILL_AUTH_001", portOneResponse.getMessage());
            }
        }
        return portOneResponse;
    }

    @RedissonLock(LockName = "결제-검증", key = "#orderRequest.orderId")
    public void webHookCheck(PortOneWebhook portOneWebhook, OrderRequest orderRequest) {
        try {
            IamportResponse<Payment> payment = iamportClient.paymentByImpUid(portOneWebhook.getImp_uid());

            checkCancelPayment(payment.getResponse().getStatus());
            checkPaymentAmount(payment.getResponse().getAmount().intValue(), orderRequest.getAmount());

            System.out.println(payment.getResponse().getPayMethod());

            if(!donationAdaptor.existsByImpId(portOneWebhook.getImp_uid())){
                User user = userAdaptor.findByUser(orderRequest.getUserId());

                Project project = projectAdaptor.findByProject(orderRequest.getProjectId());

                saveDonationUser(user, project, payment.getResponse());
            }

        } catch (IamportResponseException | IOException e) {
            throw new BadRequestException(FAILED_ERROR_AUTH);
        }
    }
}

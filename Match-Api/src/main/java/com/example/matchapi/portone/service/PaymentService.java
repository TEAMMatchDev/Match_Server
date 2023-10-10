package com.example.matchapi.portone.service;

import com.example.matchapi.order.convertor.OrderConvertor;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchapi.portone.dto.PaymentReq;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.properties.PortOneProperties;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.redis.entity.OrderRequest;
import com.example.matchdomain.redis.repository.OrderRequestRepository;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.repository.UserRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import static com.example.matchdomain.order.exception.PortOneAuthErrorCode.*;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PortOneProperties portOneProperties;
    private final OrderRequestRepository orderRequestRepository;
    private final UserRepository userRepository;
    private final OrderHelper orderHelper;
    private final DonationUserRepository donationUserRepository;
    private final OrderConvertor orderConvertor;
    private final IamportClient iamportClient;

    @Autowired
    public PaymentService(PortOneProperties portOneProperties,
                          OrderRequestRepository orderRequestRepository,
                          UserRepository userRepository,
                          OrderHelper orderHelper,
                          DonationUserRepository donationUserRepository,
                          OrderConvertor orderConvertor) {
        this.portOneProperties = portOneProperties;
        this.orderRequestRepository = orderRequestRepository;
        this.userRepository = userRepository;
        this.orderHelper = orderHelper;
        this.donationUserRepository = donationUserRepository;
        this.orderConvertor = orderConvertor;
        this.iamportClient = new IamportClient(portOneProperties.getKey(), portOneProperties.getSecret());
    }

    public void checkPayment(PaymentReq.ValidatePayment validatePayment){
        try {
            OrderRequest orderRequest = orderRequestRepository.findById(validatePayment.getOrderId()).orElseThrow(()->new BadRequestException(NOT_EXIST_ORDER_ID));
            IamportResponse<Payment> payment = iamportClient.paymentByImpUid(validatePayment.getImpUid());
            Optional<User> user = userRepository.findByIdAndStatus(Long.valueOf(orderRequest.getUserId()), Status.ACTIVE);
            int paidAmount = payment.getResponse().getAmount().intValue(); //사용자가 결제한 금액

            if(paidAmount!=validatePayment.getAmount()){
                CancelData cancelData = createCancelData(payment, 0);
                iamportClient.cancelPaymentByImpUid(cancelData);
                throw new BadRequestException(FAILED_ERROR_AUTH_AMOUNT);
            }else{
                String flameName = orderHelper.createFlameName(user.get().getName());
                String inherenceNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "." + createRandomUUID();
                donationUserRepository.save(orderConvertor.donationUserPortone(payment.getResponse(), user.get().getId(), validatePayment, Long.valueOf(orderRequest.getProjectId()), flameName, inherenceNumber));
            }
        } catch (IamportResponseException | IOException e) {
            System.out.println(e.getMessage());
            throw new BadRequestException(FAILED_ERROR_AUTH);
        }
    }

    public String createRandomUUID() {
        return UUID.randomUUID().toString();
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
}

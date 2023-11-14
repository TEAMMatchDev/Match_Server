package com.example.matchapi.portone.service;

import com.example.matchapi.donation.service.DonationHistoryService;
import com.example.matchapi.order.converter.OrderConverter;
import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchapi.portone.dto.PaymentCommand;
import com.example.matchapi.portone.dto.PaymentReq;
import com.example.matchcommon.annotation.RedissonLock;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.properties.PortOneProperties;
import com.example.matchdomain.donation.adaptor.DonationAdaptor;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.UserCard;
import com.example.matchdomain.redis.adaptor.OrderAdaptor;
import com.example.matchinfrastructure.pay.portone.client.PortOneFeignClient;
import com.example.matchinfrastructure.pay.portone.converter.PortOneConverter;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillPayResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneResponse;
import com.example.matchinfrastructure.pay.portone.service.PortOneAuthService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;

import static com.example.matchcommon.constants.MatchStatic.CANCEL_STATUS;
import static com.example.matchdomain.order.exception.PortOneAuthErrorCode.*;

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
    private IamportClient iamportClient;

    @PostConstruct
    private void init() {
        this.iamportClient = new IamportClient(portOneProperties.getKey(), portOneProperties.getSecret());
    }

    @RedissonLock(LockName = "결제-검증", key = "#paymentValidation.validatePayment.orderId")
    public OrderRes.CompleteDonation checkPayment(PaymentCommand.PaymentValidation paymentValidation){
        try {
            IamportResponse<Payment> payment = iamportClient.paymentByImpUid(paymentValidation.getValidatePayment().getImpUid());

            validatePayments(payment, paymentValidation.getValidatePayment());

            saveDonationUser(paymentValidation);

            orderAdaptor.deleteById(paymentValidation.getValidatePayment().getOrderId());

            return orderConverter.convertToCompleteDonation(paymentValidation.getUser().getName(), paymentValidation.getProject(), (long) paymentValidation.getValidatePayment().getAmount());
        } catch (IamportResponseException | IOException e) {
            throw new BadRequestException(FAILED_ERROR_AUTH);
        }
    }

    private void validatePayments(IamportResponse<Payment> payment, PaymentReq.ValidatePayment validatePayment) {
        log.info(payment.getResponse().getStatus());

        if(payment.getResponse().getStatus().equals(CANCEL_STATUS)) throw new BadRequestException(EXIST_CANCEL_STATUS);

        if(donationAdaptor.existsByImpId(validatePayment.getImpUid())) throw new BadRequestException(EXIST_IMP_UID);

        if(payment.getResponse().getAmount().intValue()!=validatePayment.getAmount()) throw new BadRequestException(FAILED_ERROR_AUTH_AMOUNT);

        if(!payment.getResponse().getMerchantUid().equals(validatePayment.getOrderId())) throw new BadRequestException(NOT_CORRECT_ORDER_ID);
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

    public void saveDonationUser(PaymentCommand.PaymentValidation paymentValidation) {
        OrderRes.CreateInherenceDto createInherenceDto = orderHelper.createInherence(paymentValidation.getUser());
        DonationUser donationUser = donationAdaptor.save(orderConverter.convertToDonationUserPortone(paymentValidation.getUser().getId(), paymentValidation.getValidatePayment(), paymentValidation.getProject().getId(), createInherenceDto));
        donationHistoryService.oneTimeDonationHistory(donationUser.getId());
    }

    public PortOneResponse<PortOneBillPayResponse> payBillKey(UserCard card, Long amount, String projectName, String type) {
        String orderId = orderHelper.createOrderId(type);
        String accessToken = portOneAuthService.getToken();
        return portOneFeignClient.payWithBillKey(accessToken, portOneConverter.convertPayWithBillKey(card.getBid(), orderId, amount, projectName, card.getCustomerId()));
    }
}

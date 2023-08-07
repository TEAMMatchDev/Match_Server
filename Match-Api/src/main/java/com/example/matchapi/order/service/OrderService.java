package com.example.matchapi.order.service;

import com.example.matchapi.order.convertor.OrderConvertor;
import com.example.matchapi.order.dto.OrderReq;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchcommon.exception.BaseException;
import com.example.matchcommon.exception.InternalServerException;
import com.example.matchcommon.properties.NicePayProperties;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.donation.repository.RegularPaymentRepository;
import com.example.matchdomain.order.entity.UserBillingCard;
import com.example.matchdomain.user.entity.User;
import com.example.matchinfrastructure.pay.nice.client.NiceAuthFeignClient;
import com.example.matchinfrastructure.pay.nice.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.example.matchdomain.order.exception.RegistrationCardErrorCode.FAILED_ERROR_ENCRYPT;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final NiceAuthFeignClient niceAuthFeignClient;
    private final NicePayProperties nicePayProperties;
    private final DonationUserRepository donationUserRepository;
    private final OrderConvertor orderConvertor;
    private final OrderHelper orderHelper;
    private final RegularPaymentRepository regularPaymentRepository;

    public NicePaymentAuth authPayment(String tid, Long amount) {
        String authorizationHeader = orderHelper.getNicePaymentAuthorizationHeader();
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.paymentAuth(authorizationHeader, tid, new NicePayRequest(String.valueOf(amount)));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth.getResultCode() , nicePaymentAuth.getResultMsg());
        return nicePaymentAuth;
    }

    public String payForProject(OrderReq.OrderDetail orderDetail) {
        String authorizationHeader = orderHelper.getNicePaymentAuthorizationHeader();
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.paymentAuth(authorizationHeader,orderDetail.getTid(), new NicePayRequest(String.valueOf(orderDetail.getAmount())));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth.getResultCode() , nicePaymentAuth.getResultMsg());

        return null;
    }




    public NicePaymentAuth cancelPayment(String tid, String orderId) {
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.cancelPayment(orderHelper.getNicePaymentAuthorizationHeader(), tid, new NicePayCancelRequest("단순취소",orderId));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth.getResultCode() , nicePaymentAuth.getResultMsg());

        return nicePaymentAuth;
    }

    @Transactional
    public String requestPayment(User user, OrderReq.OrderDetail orderDetail, Long projectId) {
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.
                paymentAuth(orderHelper.getNicePaymentAuthorizationHeader(),
                orderDetail.getTid(),
                new NicePayRequest(String.valueOf(orderDetail.getAmount())));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth.getResultCode() , nicePaymentAuth.getResultMsg());

        String flameName = orderHelper.createFlameName(user.getName());

        String inherenceNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) +"."+ createRandomUUID();

        donationUserRepository.save(orderConvertor.donationUser(nicePaymentAuth,user.getId() , orderDetail, projectId, flameName, inherenceNumber));

        return flameName;
    }

    @Transactional
    public NicePayBillkeyResponse registrationCard(User user, OrderReq.RegistrationCard registrationCard) {
        String encrypt = encrypt(orderConvertor.createPlainText(registrationCard), nicePayProperties.getSecret().substring(0, 32), nicePayProperties.getSecret().substring(0, 16));
        String orderId = createRandomUUID();


        NicePayBillkeyResponse nicePayBillkeyResponse = niceAuthFeignClient.registrationCard(
                orderHelper.getNicePaymentAuthorizationHeader(),
                new NicePayRegistrationCardRequest(encrypt,orderId,"A2"));

        //나이스 카드 확인용
        NiceBillOkResponse niceBillOkResponse = niceAuthFeignClient.billOkRequest(orderHelper.getNicePaymentAuthorizationHeader(),nicePayBillkeyResponse.getBid(), orderConvertor.niceBillOk(nicePayBillkeyResponse));

        orderHelper.checkBillResult(niceBillOkResponse.getResultCode(), niceBillOkResponse.getResultMsg(), niceBillOkResponse.getTid(), niceBillOkResponse.getOrderId());

        regularPaymentRepository.save(orderConvertor.RegularPayment(user.getId(),registrationCard,niceBillOkResponse,nicePayBillkeyResponse));

        return nicePayBillkeyResponse;
    }

    public String createRandomUUID(){
        return UUID.randomUUID().toString();
    }


    public String encrypt(String plainText, String secretKey, String iv) {
        SecretKey secureKey = new SecretKeySpec(secretKey.getBytes(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));
            byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
            return Hex.encodeHexString(encrypted);
        }catch(Exception e){
            throw new InternalServerException(FAILED_ERROR_ENCRYPT);
        }
    }



}

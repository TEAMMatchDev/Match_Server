package com.example.matchapi.order.service;

import com.example.matchapi.order.convertor.OrderConvertor;
import com.example.matchapi.order.dto.OrderReq;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchcommon.properties.NicePayProperties;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchinfrastructure.pay.nice.client.NiceAuthFeignClient;
import com.example.matchinfrastructure.pay.nice.dto.NicePayCancelRequest;
import com.example.matchinfrastructure.pay.nice.dto.NicePayRequest;
import com.example.matchinfrastructure.pay.nice.dto.NicePaymentAuth;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

import static com.example.matchcommon.constants.MatchStatic.BASIC;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final NiceAuthFeignClient niceAuthFeignClient;
    private final NicePayProperties nicePayProperties;
    private final DonationUserRepository donationUserRepository;
    private final OrderConvertor orderConvertor;
    private final OrderHelper orderHelper;

    public NicePaymentAuth authPayment(String tid, Long amount) {
        String authorizationHeader = orderHelper.getNicePaymentAuthorizationHeader();
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.paymentAuth(authorizationHeader, tid, new NicePayRequest(String.valueOf(amount)));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth);
        return nicePaymentAuth;
    }

    public String payForProject(OrderReq.OrderDetail orderDetail) {
        String authorizationHeader = orderHelper.getNicePaymentAuthorizationHeader();
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.paymentAuth(authorizationHeader,orderDetail.getTid(), new NicePayRequest(String.valueOf(orderDetail.getAmount())));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth);

        return null;
    }




    public NicePaymentAuth cancelPayment(String tid, String orderId) {
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.cancelPayment(orderHelper.getNicePaymentAuthorizationHeader(), tid, new NicePayCancelRequest("단순취소",orderId));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth);

        return nicePaymentAuth;
    }

    @Transactional
    public void requestPayment(Long id, OrderReq.OrderDetail orderDetail) {
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.paymentAuth(orderHelper.getNicePaymentAuthorizationHeader(), orderDetail.getTid(),new NicePayRequest(String.valueOf(orderDetail.getAmount())));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth);


        DonationUser donationUser = orderConvertor.donationUser(nicePaymentAuth,id,orderDetail);

        donationUser = donationUserRepository.save(donationUser);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm");


        String inherenceNumber =
                        donationUser.getCreatedAt().format(formatter)
                        +"."+ UUID.randomUUID();

        donationUser.updateInherenceNumber(inherenceNumber);

        donationUserRepository.save(donationUser);



    }
}

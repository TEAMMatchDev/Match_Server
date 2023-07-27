package com.example.matchapi.order.service;

import com.example.matchcommon.properties.NicePayProperties;
import com.example.matchinfrastructure.pay.nice.client.NiceAuthFeignClient;
import com.example.matchinfrastructure.pay.nice.dto.NicePayRequest;
import com.example.matchinfrastructure.pay.nice.dto.NicePaymentAuth;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Base64;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final NiceAuthFeignClient niceAuthFeignClient;
    private final NicePayProperties nicePayProperties;

    public NicePaymentAuth authPayment(String tid, Long amount) {
        String authorizationHeader = "Basic " + Base64.getEncoder().encodeToString((nicePayProperties.getClient() + ":" + nicePayProperties.getSecret()).getBytes());
        return niceAuthFeignClient.paymentAuth(authorizationHeader, tid, new NicePayRequest(String.valueOf(amount)));
    }
}

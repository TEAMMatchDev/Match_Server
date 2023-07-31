package com.example.matchapi.order.helper;

import com.example.matchcommon.annotation.Helper;
import com.example.matchcommon.exception.BaseException;
import com.example.matchcommon.properties.NicePayProperties;
import com.example.matchdomain.donation.entity.PayMethod;
import com.example.matchinfrastructure.pay.nice.client.NiceAuthFeignClient;
import com.example.matchinfrastructure.pay.nice.dto.NicePaymentAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Base64;

import static com.example.matchcommon.constants.MatchStatic.BASIC;

@Helper
@RequiredArgsConstructor
public class OrderHelper {
    private final NicePayProperties nicePayProperties;
    private final NiceAuthFeignClient niceAuthFeignClient;


    public PayMethod getPayMethod(String value) {
        for (PayMethod payMethod : PayMethod.values()) {
            if(payMethod.getValue().equalsIgnoreCase(value)){
                return payMethod;
            }
        }
        return null;
    }

    public String getNicePaymentAuthorizationHeader() {
        return BASIC + Base64.getEncoder().encodeToString((nicePayProperties.getClient() + ":" + nicePayProperties.getSecret()).getBytes());
    }

    public void checkNicePaymentsResult(NicePaymentAuth nicePaymentAuth){
        switch(nicePaymentAuth.getResultCode()){
            case "0000":
                break;
            default:
                throw new BaseException(HttpStatus.BAD_REQUEST,
                        false,
                        nicePaymentAuth.getResultCode(),
                        nicePaymentAuth.getResultMsg());


        }
    }
}

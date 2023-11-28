package com.example.matchinfrastructure.aligo.service;

import com.example.matchcommon.properties.AligoProperties;
import com.example.matchinfrastructure.aligo.client.AligoFeignClient;
import com.example.matchinfrastructure.aligo.client.KakaoAligoFeignClient;
import com.example.matchinfrastructure.aligo.converter.AligoConverter;
import com.example.matchinfrastructure.aligo.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.matchinfrastructure.aligo.AligoStatic.PAYMENT_TEMPLATE;

@Service
@RequiredArgsConstructor
public class AligoInfraService {
    private final AligoFeignClient aligoFeignClient;
    private final KakaoAligoFeignClient kakaoAligoFeignClient;
    private final AligoProperties aligoProperties;
    private final AligoConverter aligoConverter;

    public void sendPhone(String phone, String code) {
        String msg = "[MATCH] 회원님의 인증번호는 [" + code + "] 입니다.";
        SendRes sendRes = aligoFeignClient.sendOneMsg(aligoProperties.getKey(), aligoProperties.getUsername(),
                aligoProperties.getSender(), phone, msg);
        System.out.println(sendRes.getResultCode());
        System.out.println(sendRes.getMessage());
    }

    public void sendAlimTalkForPayment(String phone, String name){
        CreateTokenRes token = getAligoToken();

        AlimTalkReq alimTalkReq = aligoConverter.convertToPayment(phone, name, aligoProperties, token.getToken());

        System.out.println(alimTalkReq.toString());
        AligoResponse<AlimTalkRes> aligoResponse =
                kakaoAligoFeignClient.sendAlimTalk(
                        alimTalkReq
                        );
        System.out.println(aligoResponse.toString());
    }

    public void sendAlimTalkForExecution(){
        CreateTokenRes token = getAligoToken();

    }

    private CreateTokenRes getAligoToken() {
        return kakaoAligoFeignClient.createToken(aligoProperties.getKey(), aligoProperties.getUsername());
    }

}

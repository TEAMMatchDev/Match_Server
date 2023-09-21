package com.example.matchaligo.aligo.service;

import com.example.matchcommon.properties.AligoProperties;
import com.example.matchdomain.redis.entity.CodeAuth;
import com.example.matchinfrastructure.aligo.client.AligoFeignClient;
import com.example.matchinfrastructure.aligo.dto.SendRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AligoService {
    private final AligoFeignClient aligoFeignClient;
    private final AligoProperties aligoProperties;

    public void sendPhone(String phone, String code) {
        String msg = "[MATCH] 회원님의 인증번호는 [" + code + "] 입니다.";
        SendRes sendRes = aligoFeignClient.sendOneMsg(aligoProperties.getKey(), aligoProperties.getUsername(),
                aligoProperties.getSender(), phone, msg);
        System.out.println(sendRes.getResultCode());
        System.out.println(sendRes.getMessage());
    }

}

package com.example.matchapi.user.service;

import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchinfrastructure.match_aligo.client.MatchAligoFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AligoService {
    private final MatchAligoFeignClient matchAligoFeignClient;

    public CommonResponse<String> sendSmsAuth(String token, String phone, String code){
        return matchAligoFeignClient.sendSmsAuth(token, phone, code);
    }

}

package com.example.matchapi.user.service;

import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchinfrastructure.aligo.dto.AlimType;
import com.example.matchinfrastructure.match_aligo.client.MatchAligoFeignClient;
import com.example.matchinfrastructure.match_aligo.dto.AlimTalkDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AligoService {
    private final MatchAligoFeignClient matchAligoFeignClient;

    public CommonResponse<String> sendSmsAuth(String token, String phone, String code){
        return matchAligoFeignClient.sendSmsAuth(token, phone, code);
    }

    @Async("alim-talk")
    public void sendAlimTalk(String token, AlimType alimType, AlimTalkDto alimTalkDto){
        CommonResponse<String> response =  matchAligoFeignClient.sendAlimTalk(token, alimType, alimTalkDto);
        log.info(response.toString());
    }

}

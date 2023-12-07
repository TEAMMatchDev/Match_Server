package com.example.matchapi.user.service;

import com.example.matchapi.common.security.JwtService;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchinfrastructure.aligo.dto.AlimType;
import com.example.matchinfrastructure.match_aligo.client.MatchAligoFeignClient;
import com.example.matchinfrastructure.match_aligo.dto.AlimTalkDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AligoService {
    private final MatchAligoFeignClient matchAligoFeignClient;
    private final JwtService jwtService;

    public CommonResponse<String> sendSmsAuth(String phone, String code){
        return matchAligoFeignClient.sendSmsAuth(createJwt(), phone, code);
    }

    @Async("alim-talk")
    public void sendAlimTalk(AlimType alimType, AlimTalkDto alimTalkDto){
        CommonResponse<String> response =  matchAligoFeignClient.sendAlimTalk(createJwt(), alimType, alimTalkDto);
        log.info(response.toString());
    }


    public void sendAlimTalks(AlimType alimType, List<AlimTalkDto> alimTalkDtos){
        CommonResponse<String> response =  matchAligoFeignClient.sendAlimTalks(createJwt(), alimType, alimTalkDtos);
        log.info(response.toString());
    }

    public String createJwt(){
        return jwtService.createToken(1L);
    }

}

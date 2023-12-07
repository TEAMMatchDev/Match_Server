package com.example.matchinfrastructure.aligo.client;

import com.example.matchinfrastructure.aligo.config.AligoFeignConfiguration;
import com.example.matchinfrastructure.aligo.config.AligoKakaoConfiguration;
import com.example.matchinfrastructure.aligo.dto.AligoResponse;
import com.example.matchinfrastructure.aligo.dto.AlimTalkReq;
import com.example.matchinfrastructure.aligo.dto.AlimTalkRes;
import com.example.matchinfrastructure.aligo.dto.CreateTokenRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "KakaoAligoFeignClient",
        url = "https://kakaoapi.aligo.in",
        configuration = AligoKakaoConfiguration.class)
public interface KakaoAligoFeignClient {
    @PostMapping("/akv10/token/create/30/s")
    CreateTokenRes createToken(
            @RequestParam("apikey") String key,
            @RequestParam("userid") String userId
    );

    @PostMapping(value = "/akv10/alimtalk/send", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    AligoResponse<AlimTalkRes> sendAlimTalk(
            @RequestBody AlimTalkReq form
    );

}

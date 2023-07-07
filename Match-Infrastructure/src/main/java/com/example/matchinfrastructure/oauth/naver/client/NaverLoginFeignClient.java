package com.example.matchinfrastructure.oauth.naver.client;

import com.example.matchinfrastructure.oauth.kakao.dto.KakaoLoginTokenRes;
import com.example.matchinfrastructure.oauth.naver.config.NaverFeignConfiguration;
import com.example.matchinfrastructure.oauth.naver.dto.NaverTokenRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "NaverLoginFeignClient",
        url = "https://nid.naver.com",
        configuration = NaverFeignConfiguration.class)
@Component
public interface NaverLoginFeignClient {
    @PostMapping(
            "/oauth2.0/token?grant_type=authorization_code")
    NaverTokenRes naverAuth(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String client_secret,
            @RequestParam("code") String code
    );
}

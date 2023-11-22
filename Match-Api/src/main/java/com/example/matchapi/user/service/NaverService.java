package com.example.matchapi.user.service;

import com.example.matchcommon.properties.NaverProperties;
import com.example.matchinfrastructure.oauth.naver.client.NaverFeignClient;
import com.example.matchinfrastructure.oauth.naver.client.NaverLoginFeignClient;
import com.example.matchinfrastructure.oauth.naver.dto.NaverTokenRes;
import com.example.matchinfrastructure.oauth.naver.dto.NaverUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.matchcommon.constants.MatchStatic.BEARER;

@Service
@RequiredArgsConstructor
public class NaverService {
    private final NaverLoginFeignClient naverLoginFeignClient;
    private final NaverFeignClient naverFeignClient;
    private final NaverProperties naverProperties;


    public NaverTokenRes getNaverOauthToken(String code){
        return naverLoginFeignClient.naverAuth(
                naverProperties.getNaverClientId(),
                naverProperties.getNaverClientSecret(),
                code
        );
    }

    public NaverUserInfoDto getNaverUserInfo(String socialToken){
        return naverFeignClient.getInfo(BEARER + socialToken);
    }


}

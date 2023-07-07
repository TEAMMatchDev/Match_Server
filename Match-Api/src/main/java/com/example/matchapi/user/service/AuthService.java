package com.example.matchapi.user.service;

import com.example.matchapi.user.dto.UserReq;
import com.example.matchcommon.properties.KakaoProperties;
import com.example.matchcommon.properties.NaverProperties;
import com.example.matchdomain.user.repository.UserRepository;
import com.example.matchinfrastructure.oauth.kakao.client.KakaoFeignClient;
import com.example.matchinfrastructure.oauth.kakao.client.KakaoLoginFeignClient;
import com.example.matchinfrastructure.oauth.kakao.dto.KakaoLoginTokenRes;
import com.example.matchinfrastructure.oauth.kakao.dto.KakaoUserInfoDto;
import com.example.matchinfrastructure.oauth.naver.client.NaverFeignClient;
import com.example.matchinfrastructure.oauth.naver.client.NaverLoginFeignClient;
import com.example.matchinfrastructure.oauth.naver.dto.NaverTokenRes;
import com.example.matchinfrastructure.oauth.naver.dto.NaverUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.matchcommon.constants.MatchStatic.BEARER;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final KakaoFeignClient kakaoFeignClient;
    private final KakaoLoginFeignClient kakaoLoginFeignClient;
    private final NaverLoginFeignClient naverLoginFeignClient;
    private final NaverFeignClient naverFeignClient;
    private final KakaoProperties kakaoProperties;
    private final NaverProperties naverProperties;
    private final UserRepository userRepository;

    public KakaoUserInfoDto kakaoLogIn(UserReq.SocialLoginToken socialLoginToken) {
        KakaoUserInfoDto kakaoUserInfoDto = kakaoFeignClient.getInfo(BEARER + socialLoginToken.getAccessToken());


        return kakaoUserInfoDto;
    }

    public KakaoLoginTokenRes getOauthToken(String code , String referer) {

        KakaoLoginTokenRes kakaoTokenResponse = kakaoLoginFeignClient.kakaoAuth(
                kakaoProperties.getKakaoClientId(),
                kakaoProperties.getKakaoRedirectUrl(),
                kakaoProperties.getKakaoClientSecret(),
                code);




        return kakaoTokenResponse;

    }

    public String getNaverOauthToken(String code){
        NaverTokenRes naverTokenRes = naverLoginFeignClient.naverAuth(
                naverProperties.getNaverClientId(),
                naverProperties.getNaverClientSecret(),
                code
        );

        return naverTokenRes.getAccess_token();
    }

    public NaverUserInfoDto naverLogIn(UserReq.SocialLoginToken socialLoginToken) {
        return naverFeignClient.getInfo(BEARER + socialLoginToken.getAccessToken());
    }
}

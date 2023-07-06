package com.example.matchapi.user.service;

import com.example.matchapi.user.dto.UserReq;
import com.example.matchcommon.properties.OauthProperties;
import com.example.matchdomain.user.repository.UserRepository;
import com.example.matchinfrastructure.oauth.client.KakaoFeignClient;
import com.example.matchinfrastructure.oauth.client.KakaoLoginFeignClient;
import com.example.matchinfrastructure.oauth.dto.KakaoLoginTokenRes;
import com.example.matchinfrastructure.oauth.dto.KakaoTokenInfoRes;
import com.example.matchinfrastructure.oauth.dto.KakaoUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.matchcommon.constants.MatchStatic.BEARER;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final KakaoFeignClient kakaoFeignClient;
    private final KakaoLoginFeignClient kakaoLoginFeignClient;
    private final OauthProperties oauthProperties;
    private final UserRepository userRepository;

    public KakaoUserInfoDto kakaoLogIn(UserReq.SocialLoginToken socialLoginToken) {
        KakaoUserInfoDto kakaoUserInfoDto = kakaoFeignClient.getInfo(BEARER + socialLoginToken.getAccessToken());


        return kakaoUserInfoDto;
    }

    public KakaoLoginTokenRes getOauthToken(String code , String referer) {

        KakaoLoginTokenRes kakaoTokenResponse = kakaoLoginFeignClient.kakaoAuth(
                oauthProperties.getKakaoClientId(),
                oauthProperties.getKakaoRedirectUrl(),
                oauthProperties.getKakaoClientSecret(),
                code);




        return kakaoTokenResponse;

    }
}

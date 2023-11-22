package com.example.matchapi.user.service;

import com.example.matchcommon.properties.KakaoProperties;
import com.example.matchinfrastructure.oauth.kakao.client.KakaoFeignClient;
import com.example.matchinfrastructure.oauth.kakao.client.KakaoLoginFeignClient;
import com.example.matchinfrastructure.oauth.kakao.dto.KakaoLoginTokenRes;
import com.example.matchinfrastructure.oauth.kakao.dto.KakaoUserAddressDto;
import com.example.matchinfrastructure.oauth.kakao.dto.KakaoUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.matchcommon.constants.MatchStatic.BEARER;

@Service
@RequiredArgsConstructor
public class KakaoService {
    private final KakaoFeignClient kakaoFeignClient;
    private final KakaoLoginFeignClient kakaoLoginFeignClient;
    private final KakaoProperties kakaoProperties;


    public KakaoUserInfoDto getKakaoInfo(String socialToken){
        return kakaoFeignClient.getInfo(BEARER + socialToken);
    }

    public KakaoLoginTokenRes getKakaoOauthToken(String code){
        return kakaoLoginFeignClient.kakaoAuth(
                kakaoProperties.getKakaoClientId(),
                kakaoProperties.getKakaoRedirectUrl(),
                kakaoProperties.getKakaoClientSecret(),
                code);
    }

    public KakaoUserAddressDto getKakaoUserAddress(String socialToken){
        return kakaoFeignClient.getUserAddress(BEARER + socialToken);
    }


}

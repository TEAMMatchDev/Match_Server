package com.example.matchapi.user.convertor;

import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.user.entity.*;
import com.example.matchinfrastructure.oauth.kakao.dto.KakaoUserInfoDto;
import com.example.matchinfrastructure.oauth.naver.dto.NaverUserInfoDto;

import java.time.LocalDate;

@Convertor
public class UserConvertor {

    public User KakaoSignUpUser(KakaoUserInfoDto kakaoUserInfoDto, SocialType authType, String password, Gender gender, LocalDate birth, Authority authority) {
        return User.builder()
                .username(kakaoUserInfoDto.getId())
                .password(password)
                .profileImgUrl(kakaoUserInfoDto.getProfileUrl())
                .name(kakaoUserInfoDto.getName())
                .email(kakaoUserInfoDto.getEmail())
                .socialId(kakaoUserInfoDto.getId())
                .socialType(authType)
                .status(UserStatus.ACTIVE)
                .birth(birth)
                .gender(gender)
                .build();
    }

    public Authority PostAuthroity() {
        return Authority.builder()
                .authorityName("ROLE_USER")
                .build();
    }

    public User NaverSignUpUser(NaverUserInfoDto naverUserInfoDto, SocialType naver, String password, Gender gender, LocalDate birth, Authority authority) {
        return User.builder()
                .username(naverUserInfoDto.getId())
                .password(password)
                .profileImgUrl(naverUserInfoDto.getProfileImage())
                .name(naverUserInfoDto.getName())
                .email(naverUserInfoDto.getEmail())
                .socialId(naverUserInfoDto.getId())
                .socialType(naver)
                .status(UserStatus.ACTIVE)
                .birth(birth)
                .gender(gender)
                .build();
    }
}

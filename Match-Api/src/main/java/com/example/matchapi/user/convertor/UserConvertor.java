package com.example.matchapi.user.convertor;

import com.example.matchapi.user.dto.UserReq;
import com.example.matchapi.user.utils.AuthHelper;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.user.entity.*;
import com.example.matchinfrastructure.oauth.kakao.dto.KakaoUserInfoDto;
import com.example.matchinfrastructure.oauth.naver.dto.NaverUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;

@Convertor
@RequiredArgsConstructor
public class UserConvertor {
    private final AuthHelper authHelper;
    private final PasswordEncoder passwordEncoder;

    public User KakaoSignUpUser(KakaoUserInfoDto kakaoUserInfoDto, SocialType authType, Authority authority) {
        return User.builder()
                .username(kakaoUserInfoDto.getId())
                .password(authHelper.createRandomPassword())
                .profileImgUrl(kakaoUserInfoDto.getProfileUrl())
                .name(kakaoUserInfoDto.getName())
                .email(kakaoUserInfoDto.getEmail())
                .socialId(kakaoUserInfoDto.getId())
                .socialType(authType)
                .phoneNumber(kakaoUserInfoDto.getPhoneNumber().replaceAll("\\D+", "").replaceFirst("^82", "0"))
                .status(UserStatus.ACTIVE)
                .birth(authHelper.birthConversion(kakaoUserInfoDto.getBirthYear(), kakaoUserInfoDto.getBirthDay()))
                .gender(authHelper.genderConversion(kakaoUserInfoDto.getGender()))
                .authorities(Collections.singleton(authority))
                .build();
    }

    public Authority PostAuthority() {
        return Authority.builder()
                .authorityName("ROLE_USER")
                .build();
    }

    public User NaverSignUpUser(NaverUserInfoDto naverUserInfoDto, SocialType authType, Authority authority) {
        return User.builder()
                .username(naverUserInfoDto.getId())
                .password(authHelper.createRandomPassword())
                .profileImgUrl(naverUserInfoDto.getProfileImage())
                .name(naverUserInfoDto.getName())
                .email(naverUserInfoDto.getEmail())
                .socialId(naverUserInfoDto.getId())
                .socialType(authType)
                .phoneNumber(naverUserInfoDto.getMobile().replaceAll("\\D+", "").replaceFirst("^82", "0"))
                .status(UserStatus.ACTIVE)
                .birth(authHelper.birthConversion(naverUserInfoDto.getBirthyear(), naverUserInfoDto.getBirthday()))
                .gender(authHelper.genderConversion(naverUserInfoDto.getGender()))
                .authorities(Collections.singleton(authority))
                .build();
    }

    public User SignUpUser(UserReq.SignUpUser signUpUser, Authority authority) {
        return User.builder()
               .username(signUpUser.getEmail())
               .password(passwordEncoder.encode(signUpUser.getPassword()))
               .name(signUpUser.getName())
               .email(signUpUser.getEmail())
               .socialType(SocialType.NORMAL)
               .phoneNumber(signUpUser.getPhone())
               .status(UserStatus.ACTIVE)
               .birth(LocalDate.parse(signUpUser.getBirthDate()))
               .gender(authHelper.genderConversion(signUpUser.getGender()))
                .authorities(Collections.singleton(authority))
                .build();
    }
}

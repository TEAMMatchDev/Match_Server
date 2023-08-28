package com.example.matchapi.user.convertor;

import com.example.matchapi.user.dto.UserReq;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchapi.user.helper.AuthHelper;
import com.example.matchapi.user.helper.UserHelper;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.redis.entity.RefreshToken;
import com.example.matchdomain.user.entity.*;
import com.example.matchinfrastructure.oauth.kakao.dto.KakaoUserAddressDto;
import com.example.matchinfrastructure.oauth.kakao.dto.KakaoUserInfoDto;
import com.example.matchinfrastructure.oauth.naver.dto.NaverUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Convertor
@RequiredArgsConstructor
public class UserConvertor {
    private final AuthHelper authHelper;
    private final UserHelper userHelper;
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
                .role(AuthorityEnum.ROLE_USER.getValue())
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
                .role(AuthorityEnum.ROLE_USER.getValue())
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
                .birth(authHelper.birthConversionToLocalDate(signUpUser.getBirthDate()))
                .gender(signUpUser.getGender())
                .role(AuthorityEnum.ROLE_USER.getValue())
                .build();
    }

    public UserRes.EditMyPage toMyPage(User user) {
        return UserRes.EditMyPage.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhoneNumber())
                .gender(user.getGender().getValue())
                .birthDate(userHelper.birthConversion(user.getBirth()))
                .build();
    }

    public UserAddress AddUserAddress(Long userId, KakaoUserAddressDto.ShippingAddresses shippingAddresses) {
        return UserAddress.builder()
                .userId(userId)
                .name(shippingAddresses.getName())
                .isDefault(shippingAddresses.isDefault())
                .addressType(AddressType.valueOf(shippingAddresses.getType()))
                .baseAddress(shippingAddresses.getBaseAddress())
                .detailAddress(shippingAddresses.getDetailAddress())
                .receiverName(shippingAddresses.getReceiverName())
                .addressPhoneNumber(shippingAddresses.getReceiverPhoneNumber1())
                .zoneNumber(shippingAddresses.getZoneNumber())
                .zipCode(shippingAddresses.getZipCode())
                .build();
    }

    public RefreshToken RefreshToken(Long userId, String refreshToken, Long refreshTokenSeconds) {
        return RefreshToken.builder()
                .userId(String.valueOf(userId))
                .token(refreshToken)
                .ttl(refreshTokenSeconds)
                .build();
    }
}

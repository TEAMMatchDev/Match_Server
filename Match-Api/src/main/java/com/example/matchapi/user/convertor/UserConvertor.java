package com.example.matchapi.user.convertor;

import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.user.dto.UserReq;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchapi.user.helper.AuthHelper;
import com.example.matchapi.user.helper.UserHelper;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchcommon.properties.AligoProperties;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.redis.entity.RefreshToken;
import com.example.matchdomain.user.entity.*;
import com.example.matchdomain.user.entity.pk.UserFcmPk;
import com.example.matchdomain.user.repository.UserRepository;
import com.example.matchinfrastructure.aligo.dto.SendReq;
import com.example.matchinfrastructure.oauth.kakao.dto.KakaoUserAddressDto;
import com.example.matchinfrastructure.oauth.kakao.dto.KakaoUserInfoDto;
import com.example.matchinfrastructure.oauth.naver.dto.NaverUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

import static com.example.matchcommon.constants.MatchStatic.BASE_PROFILE;

@Convertor
@RequiredArgsConstructor
public class UserConvertor {
    private final AuthHelper authHelper;
    private final UserHelper userHelper;
    private final PasswordEncoder passwordEncoder;
    private final AligoProperties aligoProperties;

    public User KakaoSignUpUser(KakaoUserInfoDto kakaoUserInfoDto, SocialType authType, Authority authority) {
        String profileImg = BASE_PROFILE;
        if(kakaoUserInfoDto.getProfileUrl() != null){
            profileImg = kakaoUserInfoDto.getProfileUrl();
        }
        return User.builder()
                .username(kakaoUserInfoDto.getId())
                .password(authHelper.createRandomPassword())
                .profileImgUrl(profileImg)
                .name(kakaoUserInfoDto.getName())
                .email(kakaoUserInfoDto.getEmail())
                .socialId(kakaoUserInfoDto.getId())
                .socialType(authType)
                .phoneNumber(kakaoUserInfoDto.getPhoneNumber().replaceAll("\\D+", "").replaceFirst("^82", "0"))
                .birth(authHelper.birthConversion(kakaoUserInfoDto.getBirthYear(), kakaoUserInfoDto.getBirthDay()))
                .gender(authHelper.genderConversion(kakaoUserInfoDto.getGender()))
                .role(AuthorityEnum.ROLE_USER.getValue())
                .nickname(kakaoUserInfoDto.getProperties().getNickname())
                .build();
    }

    public Authority PostAuthority() {
        return Authority.builder()
                .authorityName("ROLE_USER")
                .build();
    }

    public User NaverSignUpUser(NaverUserInfoDto naverUserInfoDto, SocialType authType, Authority authority) {
        String profileImg = BASE_PROFILE;
        if(naverUserInfoDto.getProfileImage() != null){
            profileImg = naverUserInfoDto.getProfileImage();
        }
        return User.builder()
                .username(naverUserInfoDto.getId())
                .password(authHelper.createRandomPassword())
                .profileImgUrl(profileImg)
                .name(naverUserInfoDto.getName())
                .email(naverUserInfoDto.getEmail())
                .socialId(naverUserInfoDto.getId())
                .socialType(authType)
                .phoneNumber(naverUserInfoDto.getMobile().replaceAll("\\D+", "").replaceFirst("^82", "0"))
                .birth(authHelper.birthConversion(naverUserInfoDto.getBirthyear(), naverUserInfoDto.getBirthday()))
                .gender(authHelper.genderConversion(naverUserInfoDto.getGender()))
                .role(AuthorityEnum.ROLE_USER.getValue())
                .nickname(naverUserInfoDto.getNickname())
                .build();
    }

    public User SignUpUser(UserReq.SignUpUser signUpUser, Authority authority) {
        return User.builder()
                .username(signUpUser.getEmail())
                .profileImgUrl(BASE_PROFILE)
                .password(passwordEncoder.encode(signUpUser.getPassword()))
                .name(signUpUser.getName())
                .email(signUpUser.getEmail())
                .socialType(SocialType.NORMAL)
                .phoneNumber(signUpUser.getPhone())
                .birth(authHelper.birthConversionToLocalDate(signUpUser.getBirthDate()))
                .gender(signUpUser.getGender())
                .role(AuthorityEnum.ROLE_USER.getValue())
                .nickname(signUpUser.getName())
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

    public OrderRes.UserDetail userInfo(User user) {
        return OrderRes.UserDetail.builder()
                .name(user.getName())
                .birthDay(user.getBirth().toString())
                .phoneNumber(user.getPhoneNumber()).build();
    }

    public UserRes.SignUpInfo UserSignUpInfo(Long oneDayUser, Long weekUser, Long monthUser, Long totalUser) {
        return UserRes.SignUpInfo.builder()
                .totalUserCnt(totalUser)
                .oneDayUserCnt(oneDayUser)
                .weekUserCnt(weekUser)
                .monthUserCnt(monthUser)
                .build();
    }

    public UserRes.UserList UserList(UserRepository.UserList result) {
        return UserRes.UserList
                .builder()
                .userId(result.getUserId())
                .name(result.getName())
                .birth(String.valueOf(result.getBirth()))
                .socialType(result.getSocialType().getName())
                .gender(result.getGender().getValue())
                .email(result.getEmail())
                .phoneNumber(result.getPhoneNumber())
                .donationCnt(result.getDonationCnt())
                .totalAmount(result.getTotalAmount())
                .card(result.getCard())
                .status(result.getStatus().getValue())
                .createdAt(result.getCreatedAt().toString())
                .build();
    }

    public UserRes.UserAdminDetail UserAdminDetail(UserRepository.UserList userDetail, List<OrderRes.UserBillCard> userCards) {
        return UserRes.UserAdminDetail
                .builder()
                .userId(userDetail.getUserId())
                .name(userDetail.getName())
                .birth(String.valueOf(userDetail.getBirth()))
                .socialType(userDetail.getSocialType().getName())
                .gender(userDetail.getGender().getValue())
                .email(userDetail.getEmail())
                .phoneNumber(userDetail.getPhoneNumber())
                .donationCnt(userDetail.getDonationCnt())
                .totalAmount(userDetail.getTotalAmount())
                .card(userDetail.getCard())
                .status(userDetail.getStatus().getValue())
                .createdAt(userDetail.getCreatedAt().toString())
                //.userCards(userCards)
                .build();
    }

    public SendReq SendSms(String phone, String code) {
        return SendReq
                .builder()
                .key(aligoProperties.getKey())
                .sender(aligoProperties.getSender())
                .userId(aligoProperties.getUsername())
                .msg("[MATCH] 회원님의 인증번호는 [" + code + "] 입니다.")
                .receiver(phone)
                .build();
    }

    public UserRes.Profile UserProfile(User user) {
        return UserRes.Profile
                .builder()
                .profileImgUrl(user.getProfileImgUrl())
                .name(user.getName())
                .socialType(user.getSocialType())
                .email(user.getEmail())
                .phone(user.getPhoneNumber())
                .build();
    }

    public UserFcmToken UserFcm(User user, UserReq.FcmToken token) {
        return UserFcmToken.builder()
                .userFcmPk(
                        UserFcmPk.builder()
                                .userId(user.getId())
                                .deviceId(token.getDeviceId())
                                .build())
                .fcmToken(token.getFcmToken())
                .build();
    }
}

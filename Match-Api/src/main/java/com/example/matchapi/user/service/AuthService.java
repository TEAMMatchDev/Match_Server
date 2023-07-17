package com.example.matchapi.user.service;

import com.example.matchapi.security.JwtService;
import com.example.matchapi.user.convertor.UserConvertor;
import com.example.matchapi.user.dto.UserReq;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchapi.user.utils.AuthHelper;
import com.example.matchcommon.properties.KakaoProperties;
import com.example.matchcommon.properties.NaverProperties;
import com.example.matchdomain.user.entity.Authority;
import com.example.matchdomain.user.entity.Gender;
import com.example.matchdomain.user.entity.User;
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
import net.nurigo.java_sdk.exceptions.CoolsmsException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import static com.example.matchcommon.constants.MatchStatic.BEARER;
import static com.example.matchdomain.user.entity.SocialType.KAKAO;
import static com.example.matchdomain.user.entity.SocialType.NAVER;

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
    private final JwtService jwtService;
    private final AuthHelper authHelper;
    private final UserConvertor userConvertor;


    public UserRes.UserToken kakaoLogIn(UserReq.SocialLoginToken socialLoginToken) {
        KakaoUserInfoDto kakaoUserInfoDto = kakaoFeignClient.getInfo(BEARER + socialLoginToken.getAccessToken());
        Long userId;

        Optional<User> user = userRepository.findBySocialIdAndSocialType(kakaoUserInfoDto.getId(), KAKAO);
        authHelper.checkUserExists(kakaoUserInfoDto.getPhoneNumber(), KAKAO);

        //카카오 전화번호로 이미 다른 소셜로그인 이나 기본가입을 했던 사람.


        //소셜 로그인 정보가 없을 시
        if (user.isEmpty()) userId = kakaoSignUp(kakaoUserInfoDto);
        //소셜 로그인 정보가 있을 시
        else userId = user.get().getId();


        return new UserRes.UserToken(userId, jwtService.createToken(userId), jwtService.createRefreshToken(userId));
    }


    private Long kakaoSignUp(KakaoUserInfoDto kakaoUserInfoDto) {

        Authority authority = userConvertor.PostAuthroity();
        String password = authHelper.createRandomPassword();
        Gender gender = authHelper.genderConversion(kakaoUserInfoDto.getGender());
        LocalDate birth = authHelper.birthConversion(kakaoUserInfoDto.getBirthYear(), kakaoUserInfoDto.getBirthDay());
        User user = userConvertor.KakaoSignUpUser(kakaoUserInfoDto, KAKAO, password, gender, birth, authority);

        System.out.println(kakaoUserInfoDto.getPhoneNumber());

        return userRepository.save(user).getId();
    }

    public KakaoLoginTokenRes getOauthToken(String code, String referer) {
        System.out.println(kakaoProperties.getKakaoRedirectUrl());
        return kakaoLoginFeignClient.kakaoAuth(
                kakaoProperties.getKakaoClientId(),
                kakaoProperties.getKakaoRedirectUrl(),
                kakaoProperties.getKakaoClientSecret(),
                code);

    }

    public String getNaverOauthToken(String code) {
        System.out.println(code);
        NaverTokenRes naverTokenRes = naverLoginFeignClient.naverAuth(
                naverProperties.getNaverClientId(),
                naverProperties.getNaverClientSecret(),
                code
        );

        return naverTokenRes.getAccess_token();
    }

    public UserRes.UserToken naverLogIn(UserReq.SocialLoginToken socialLoginToken) {
        NaverUserInfoDto naverUserInfoDto = naverFeignClient.getInfo(BEARER + socialLoginToken.getAccessToken());
        Long userId;
        authHelper.checkUserExists(naverUserInfoDto.getMobile(), NAVER);

        Optional<User> user = userRepository.findBySocialIdAndSocialType(naverUserInfoDto.getResponse().getId(), NAVER);

        if (user.isEmpty()) userId = naverSignUp(naverUserInfoDto);

        else userId = user.get().getId();

        return new UserRes.UserToken(userId, jwtService.createToken(userId), jwtService.createRefreshToken(userId));
    }

    private Long naverSignUp(NaverUserInfoDto naverUserInfoDto) {
        Authority authority = userConvertor.PostAuthroity();
        String password = authHelper.createRandomPassword();
        Gender gender = authHelper.genderConversion(naverUserInfoDto.getGender());
        LocalDate birth = authHelper.birthConversion(naverUserInfoDto.getBirthyear(), naverUserInfoDto.getBirthday());
        User user = userConvertor.NaverSignUpUser(naverUserInfoDto, NAVER, password, gender, birth, authority);

        return userRepository.save(user).getId();
    }

    public String checkSms(String phone) throws CoolsmsException {
        String number = authHelper.createRandomNumber();
        authHelper.sendSms(phone,number);
        return number;
    }
}

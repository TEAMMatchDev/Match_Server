package com.example.matchapi.user.service;

import com.example.matchapi.security.JwtService;
import com.example.matchapi.user.convertor.UserConvertor;
import com.example.matchapi.user.dto.UserReq;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchapi.user.utils.AuthHelper;
import com.example.matchapi.user.utils.SmsHelper;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.properties.KakaoProperties;
import com.example.matchcommon.properties.NaverProperties;
import com.example.matchdomain.user.entity.Authority;
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

import java.util.Optional;

import static com.example.matchcommon.constants.MatchStatic.BEARER;
import static com.example.matchcommon.exception.CommonResponseStatus.*;
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
    private final SmsHelper smsHelper;


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

        Authority authority = userConvertor.PostAuthority();
        User user = userConvertor.KakaoSignUpUser(kakaoUserInfoDto, KAKAO, authority);

        System.out.println(kakaoUserInfoDto.getPhoneNumber());

        return userRepository.save(user).getId();
    }

    private Long naverSignUp(NaverUserInfoDto naverUserInfoDto) {
        Authority authority = userConvertor.PostAuthority();
        User user = userConvertor.NaverSignUpUser(naverUserInfoDto, NAVER, authority);

        return userRepository.save(user).getId();
    }
    public KakaoLoginTokenRes getOauthToken(String code, String referer) {
        return kakaoLoginFeignClient.kakaoAuth(
                kakaoProperties.getKakaoClientId(),
                kakaoProperties.getKakaoRedirectUrl(),
                kakaoProperties.getKakaoClientSecret(),
                code);

    }

    public String getNaverOauthToken(String code) {
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



    public UserRes.UserToken signUpUser(UserReq.SignUpUser signUpUser) {
        if(userRepository.existsByPhoneNumber(signUpUser.getPhone())) throw new BadRequestException(USERS_EXISTS_PHONE);
        if(userRepository.existsByEmail(signUpUser.getEmail())) throw new BadRequestException(USERS_EXISTS_EMAIL);

        Authority authority = userConvertor.PostAuthority();

        Long userId = userRepository.save(userConvertor.SignUpUser(signUpUser,authority)).getId();

        return new UserRes.UserToken(userId, jwtService.createToken(userId), jwtService.createRefreshToken(userId));
    }

    public void checkUserPhone(UserReq.UserPhone userPhone) {
        if(userRepository.existsByPhoneNumber(userPhone.getPhone())) throw new BadRequestException(USERS_EXISTS_PHONE);
    }

    public void checkUserEmail(UserReq.UserEmail userEmail) {
        if(userRepository.existsByEmail(userEmail.getEmail())) throw new BadRequestException(USERS_EXISTS_EMAIL);
    }
}

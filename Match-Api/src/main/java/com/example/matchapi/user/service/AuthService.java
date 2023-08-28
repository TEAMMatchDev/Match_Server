package com.example.matchapi.user.service;

import com.example.matchapi.security.JwtService;
import com.example.matchapi.user.convertor.UserConvertor;
import com.example.matchapi.user.dto.UserReq;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchapi.user.helper.AuthHelper;
import com.example.matchapi.user.helper.SmsHelper;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.UnauthorizedException;
import com.example.matchcommon.properties.JwtProperties;
import com.example.matchcommon.properties.KakaoProperties;
import com.example.matchcommon.properties.NaverProperties;
import com.example.matchdomain.redis.repository.RefreshTokenRepository;
import com.example.matchdomain.user.entity.Authority;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.UserAddress;
import com.example.matchdomain.user.repository.UserAddressRepository;
import com.example.matchdomain.user.repository.UserRepository;
import com.example.matchinfrastructure.oauth.kakao.client.KakaoFeignClient;
import com.example.matchinfrastructure.oauth.kakao.client.KakaoLoginFeignClient;
import com.example.matchinfrastructure.oauth.kakao.dto.KakaoLoginTokenRes;
import com.example.matchinfrastructure.oauth.kakao.dto.KakaoUserAddressDto;
import com.example.matchinfrastructure.oauth.kakao.dto.KakaoUserInfoDto;
import com.example.matchinfrastructure.oauth.naver.client.NaverFeignClient;
import com.example.matchinfrastructure.oauth.naver.client.NaverLoginFeignClient;
import com.example.matchinfrastructure.oauth.naver.dto.NaverAddressDto;
import com.example.matchinfrastructure.oauth.naver.dto.NaverTokenRes;
import com.example.matchinfrastructure.oauth.naver.dto.NaverUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.matchcommon.constants.MatchStatic.BEARER;
import static com.example.matchcommon.exception.errorcode.CommonResponseStatus.*;
import static com.example.matchdomain.user.entity.SocialType.KAKAO;
import static com.example.matchdomain.user.entity.SocialType.NAVER;
import static com.example.matchdomain.user.exception.UserAuthErrorCode.NOT_EXIST_USER;
import static com.example.matchdomain.user.exception.UserLoginErrorCode.NOT_CORRECT_PASSWORD;
import static com.example.matchdomain.user.exception.UserNormalSignUpErrorCode.USERS_EXISTS_EMAIL;
import static com.example.matchdomain.user.exception.UserNormalSignUpErrorCode.USERS_EXISTS_PHONE;

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
    private final UserAddressRepository userAddressRepository;
    private final JwtService jwtService;
    private final AuthHelper authHelper;
    private final UserConvertor userConvertor;
    private final PasswordEncoder passwordEncoder;
    private final SmsHelper smsHelper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;


    @Transactional
    public UserRes.UserToken kakaoLogIn(UserReq.SocialLoginToken socialLoginToken) {
        KakaoUserInfoDto kakaoUserInfoDto = kakaoFeignClient.getInfo(BEARER + socialLoginToken.getAccessToken());

        Long userId;
        Optional<User> user = userRepository.findBySocialIdAndSocialType(kakaoUserInfoDto.getId(), KAKAO);
        authHelper.checkUserExists(kakaoUserInfoDto.getPhoneNumber(), KAKAO);

        //소셜 로그인 정보가 없을 시
        if (user.isEmpty()){
            userId = kakaoSignUp(kakaoUserInfoDto);
            KakaoUserAddressDto kakaoUserAddressDto = kakaoFeignClient.getUserAddress(BEARER+socialLoginToken.getAccessToken());
            if(!kakaoUserAddressDto.isShippingAddressesNeedsAgreement()){
                List<UserAddress> userAddressList = new ArrayList<>();
                for(KakaoUserAddressDto.ShippingAddresses shippingAddresses : kakaoUserAddressDto.getShippingAddresses()){
                    UserAddress userAddress = userConvertor.AddUserAddress(userId,shippingAddresses);
                    userAddressList.add(userAddress);
                }
                userAddressRepository.saveAll(userAddressList);
            }
        }
        //소셜 로그인 정보가 있을 시
        else {
            authHelper.checkUserExists(kakaoUserInfoDto.getPhoneNumber(), KAKAO);
            userId = user.get().getId();
        }
        
        UserRes.Token token = createToken(userId);
        

        return new UserRes.UserToken(userId, token.getAccessToken(), token.getRefreshToken());
    }

    private UserRes.Token createToken(Long userId) {
        UserRes.Token token =  jwtService.createTokens(userId);
        refreshTokenRepository.save(userConvertor.RefreshToken(userId,token.getRefreshToken(),jwtProperties.getRefreshTokenSeconds()));
        return token;
    }


    private Long kakaoSignUp(KakaoUserInfoDto kakaoUserInfoDto) {

        Authority authority = userConvertor.PostAuthority();
        User user = userConvertor.KakaoSignUpUser(kakaoUserInfoDto, KAKAO, authority);

        System.out.println(kakaoUserInfoDto.getPhoneNumber());

        return userRepository.save(user).getId();
    }

    @Transactional
    public Long naverSignUp(NaverUserInfoDto naverUserInfoDto) {
        return userRepository.save(userConvertor.NaverSignUpUser(naverUserInfoDto, NAVER, userConvertor.PostAuthority())).getId();
    }
    public KakaoLoginTokenRes getOauthToken(String code, String referer) {
        return kakaoLoginFeignClient.kakaoAuth(
                kakaoProperties.getKakaoClientId(),
                kakaoProperties.getKakaoRedirectUrl(),
                kakaoProperties.getKakaoClientSecret(),
                code);

    }

    public UserRes.UserToken getNaverOauthToken(String code) {
        NaverTokenRes naverTokenRes = naverLoginFeignClient.naverAuth(
                naverProperties.getNaverClientId(),
                naverProperties.getNaverClientSecret(),
                code
        );

        return naverLogIn(new UserReq.SocialLoginToken(naverTokenRes.getAccess_token()));
    }

    public UserRes.UserToken naverLogIn(UserReq.SocialLoginToken socialLoginToken) {
        NaverUserInfoDto naverUserInfoDto = naverFeignClient.getInfo(BEARER + socialLoginToken.getAccessToken());
        Long userId;
        authHelper.checkUserExists(naverUserInfoDto.getMobile(), NAVER);

        Optional<User> user = userRepository.findBySocialIdAndSocialType(naverUserInfoDto.getResponse().getId(), NAVER);

        if (user.isEmpty()) userId = naverSignUp(naverUserInfoDto);

        else userId = user.get().getId();

        UserRes.Token token = createToken(userId);
        
        return new UserRes.UserToken(userId, token.getAccessToken(), token.getRefreshToken());
    }



    public UserRes.UserToken signUpUser(UserReq.SignUpUser signUpUser) {
        if(userRepository.existsByPhoneNumber(signUpUser.getPhone())) throw new BadRequestException(USERS_EXISTS_PHONE);
        if(userRepository.existsByEmail(signUpUser.getEmail())) throw new BadRequestException(USERS_EXISTS_EMAIL);

        Authority authority = userConvertor.PostAuthority();

        Long userId = userRepository.save(userConvertor.SignUpUser(signUpUser,authority)).getId();

        UserRes.Token token = createToken(userId);


        return new UserRes.UserToken(userId, token.getAccessToken(), token.getRefreshToken());
    }

    public void checkUserPhone(UserReq.UserPhone userPhone) {
        if(userRepository.existsByPhoneNumber(userPhone.getPhone())) throw new BadRequestException(USERS_EXISTS_PHONE);
    }

    public void checkUserEmail(UserReq.UserEmail userEmail) {
        if(userRepository.existsByEmail(userEmail.getEmail())) throw new BadRequestException(USERS_EXISTS_EMAIL);
    }

    public UserRes.UserToken logIn(UserReq.LogIn logIn) {
        User user=userRepository.findByUsername(logIn.getEmail()).orElseThrow(() -> new UnauthorizedException(NOT_EXIST_USER));

        if(!passwordEncoder.matches(logIn.getPassword(),user.getPassword())) throw new BadRequestException(NOT_CORRECT_PASSWORD);

        Long userId = user.getId();

        UserRes.Token token = createToken(userId);

        //반환 값 아이디 추가
        return new UserRes.UserToken(userId, token.getAccessToken(), token.getRefreshToken());
    }

    public KakaoUserAddressDto getKakaoAddress(String accessToken) {
        return kakaoFeignClient.getUserAddress(BEARER + accessToken);
    }


    public NaverAddressDto getNaverAddress(String accessToken) {
        return naverFeignClient.getUserAddress(BEARER + accessToken);
    }


}

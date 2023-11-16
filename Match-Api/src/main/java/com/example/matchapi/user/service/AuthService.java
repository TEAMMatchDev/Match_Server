package com.example.matchapi.user.service;

import com.example.matchapi.common.security.JwtService;
import com.example.matchapi.user.converter.UserConverter;
import com.example.matchapi.user.dto.UserReq;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchapi.user.helper.AuthHelper;
import com.example.matchapi.user.helper.SmsHelper;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.UnauthorizedException;
import com.example.matchcommon.properties.JwtProperties;
import com.example.matchcommon.properties.KakaoProperties;
import com.example.matchcommon.properties.NaverProperties;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.service.MailService;
import com.example.matchdomain.redis.entity.CodeAuth;
import com.example.matchdomain.redis.repository.CodeAuthRepository;
import com.example.matchdomain.redis.repository.RefreshTokenRepository;
import com.example.matchdomain.user.adaptor.UserAdaptor;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.UserAddress;
import com.example.matchdomain.user.repository.UserAddressRepository;
import com.example.matchdomain.user.repository.UserRepository;
import com.example.matchinfrastructure.match_aligo.client.MatchAligoFeignClient;
import com.example.matchinfrastructure.oauth.apple.dto.AppleUserRes;
import com.example.matchinfrastructure.oauth.apple.service.AppleAuthService;
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
import static com.example.matchdomain.common.model.Status.ACTIVE;
import static com.example.matchdomain.user.entity.enums.AuthorityEnum.ROLE_ADMIN;
import static com.example.matchdomain.user.entity.enums.SocialType.*;
import static com.example.matchdomain.user.exception.AdminLoginErrorCode.NOT_ADMIN;
import static com.example.matchdomain.user.exception.CodeAuthErrorCode.NOT_CORRECT_AUTH;
import static com.example.matchdomain.user.exception.CodeAuthErrorCode.NOT_CORRECT_CODE;
import static com.example.matchdomain.user.exception.SendEmailFindPassword.NOT_EXISTS_EMAIL;
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
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final SmsHelper smsHelper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;
    private final MailService mailService;
    private final CodeAuthRepository codeAuthRepository;
    private final MatchAligoFeignClient matchAligoFeignClient;
    private final AppleAuthService authService;
    private final UserAdaptor userAdaptor;


    @Transactional
    public UserRes.UserToken kakaoLogIn(UserReq.SocialLoginToken socialLoginToken) {
        KakaoUserInfoDto kakaoUserInfoDto = kakaoFeignClient.getInfo(BEARER + socialLoginToken.getAccessToken());

        Long userId;
        Optional<User> user = userRepository.findBySocialIdAndSocialTypeAndStatus(kakaoUserInfoDto.getId(), KAKAO, ACTIVE);
        authHelper.checkUserExists(kakaoUserInfoDto.getPhoneNumber(), KAKAO);

        //소셜 로그인 정보가 없을 시
        if (user.isEmpty()){
            userId = kakaoSignUp(kakaoUserInfoDto);
            KakaoUserAddressDto kakaoUserAddressDto = kakaoFeignClient.getUserAddress(BEARER+socialLoginToken.getAccessToken());
            if(!kakaoUserAddressDto.isShippingAddressesNeedsAgreement()){
                List<UserAddress> userAddressList = new ArrayList<>();
                for(KakaoUserAddressDto.ShippingAddresses shippingAddresses : kakaoUserAddressDto.getShippingAddresses()){
                    UserAddress userAddress = userConverter.convertToAddUserAddress(userId,shippingAddresses);
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
        return jwtService.createTokens(userId);
    }


    private Long kakaoSignUp(KakaoUserInfoDto kakaoUserInfoDto) {
        User user = userConverter.convertToKakaoSignUpUser(kakaoUserInfoDto, KAKAO);

        System.out.println(kakaoUserInfoDto.getPhoneNumber());

        return userRepository.save(user).getId();
    }

    @Transactional
    public Long naverSignUp(NaverUserInfoDto naverUserInfoDto) {
        return userRepository.save(userConverter.convertToNaverSignUpUser(naverUserInfoDto, NAVER)).getId();
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

        Optional<User> user = userRepository.findBySocialIdAndSocialTypeAndStatus(naverUserInfoDto.getResponse().getId(), NAVER, ACTIVE);

        if (user.isEmpty()) userId = naverSignUp(naverUserInfoDto);

        else userId = user.get().getId();

        UserRes.Token token = createToken(userId);
        
        return new UserRes.UserToken(userId, token.getAccessToken(), token.getRefreshToken());
    }



    public UserRes.UserToken signUpUser(UserReq.SignUpUser signUpUser) {
        if(userAdaptor.existsPhoneNumber(signUpUser.getPhone())) throw new BadRequestException(USERS_EXISTS_PHONE);
        if(userAdaptor.existsEmail(signUpUser.getEmail())) throw new BadRequestException(USERS_EXISTS_EMAIL);

        Long userId = userRepository.save(userConverter.convertToSignUpUser(signUpUser)).getId();

        UserRes.Token token = createToken(userId);


        return new UserRes.UserToken(userId, token.getAccessToken(), token.getRefreshToken());
    }

    public void checkUserPhone(UserReq.UserPhone userPhone) {
        if(userAdaptor.existsPhoneNumber(userPhone.getPhone())) throw new BadRequestException(USERS_EXISTS_PHONE);
    }

    public void checkUserEmail(UserReq.UserEmail userEmail) {
        if(userAdaptor.existsEmail(userEmail.getEmail())) throw new BadRequestException(USERS_EXISTS_EMAIL);
    }

    public UserRes.UserToken logIn(UserReq.LogIn logIn) {
        User user=userRepository.findByUsernameAndStatus(logIn.getEmail(), ACTIVE).orElseThrow(() -> new UnauthorizedException(NOT_EXIST_USER));

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


    public UserRes.UserToken adminLogIn(UserReq.LogIn logIn) {
        User user=userRepository.findByUsername(logIn.getEmail()).orElseThrow(() -> new UnauthorizedException(NOT_EXIST_USER));

        if(!passwordEncoder.matches(logIn.getPassword(),user.getPassword())) throw new BadRequestException(NOT_CORRECT_PASSWORD);
        if(!user.getRole().contains(ROLE_ADMIN.getValue())) throw new BadRequestException(NOT_ADMIN);

        Long userId = user.getId();

        UserRes.Token token = createToken(userId);

        return new UserRes.UserToken(userId, token.getAccessToken(), token.getRefreshToken());
    }

    public void sendEmailMessage(String email) {
        checkUserEmail(new UserReq.UserEmail(email));

        String code = smsHelper.createRandomNumber();

        codeAuthRepository.save(CodeAuth.builder().auth(email).code(code).ttl(300).build());

        mailService.sendEmailMessage(email, code);
    }

    public void checkUserEmailAuth(UserReq.UserEmailAuth email) {
        CodeAuth codeAuth = codeAuthRepository.findById(email.getEmail()).orElseThrow(()->new BadRequestException(NOT_CORRECT_AUTH));
        if(!codeAuth.getCode().equals(email.getCode()))throw new BadRequestException(NOT_CORRECT_CODE);
    }

    public void sendPhone(String phone) {
        checkUserPhone(new UserReq.UserPhone(phone));
        String code = smsHelper.createRandomNumber();
        codeAuthRepository.save(CodeAuth.builder().auth(phone).code(code).ttl(300).build());
        CommonResponse<String> sendRes = matchAligoFeignClient.sendSmsAuth(jwtService.createToken(1L), phone, code);
    }

    public void checkPhoneAuth(UserReq.UserPhoneAuth phone) {
        CodeAuth codeAuth = codeAuthRepository.findById(phone.getPhone()).orElseThrow(()->new BadRequestException(NOT_CORRECT_AUTH));
        if(!codeAuth.getCode().equals(phone.getCode()))throw new BadRequestException(NOT_CORRECT_CODE);
    }

    public UserRes.UserToken appleLogin(UserReq.SocialLoginToken socialLoginToken) {
        AppleUserRes appleUserRes = authService.appleLogin(socialLoginToken.getAccessToken());

        if(userRepository.existsByEmailAndSocialTypeNot(appleUserRes.getEmail(), APPLE)) throw new BadRequestException(USERS_EXISTS_EMAIL);
        Optional<User> user = userAdaptor.existsSocialUser(appleUserRes.getSocialId(), APPLE);

        Long userId;
        if (user.isEmpty()) userId = appleSignUp(appleUserRes);

        else userId = user.get().getId();

        UserRes.Token token = createToken(userId);

        return new UserRes.UserToken(userId, token.getAccessToken(), token.getRefreshToken());
    }

    private Long appleSignUp(AppleUserRes appleUserRes) {
        return userRepository.save(userConverter.convertToAppleUserSignUp(appleUserRes)).getId();
    }


    public void sendEmailPasswordFind(String email) {
        if(!userAdaptor.checkEmailPassword(email, NORMAL)) throw new BadRequestException(NOT_EXISTS_EMAIL);

        String code = smsHelper.createRandomNumber();

        codeAuthRepository.save(CodeAuth.builder().auth(email).code(code).ttl(300).build());

        mailService.sendEmailMessage(email, code);
    }

    public void modifyPassword(UserReq.FindPassword findPassword) {
        CodeAuth codeAuth = codeAuthRepository.findById(findPassword.getEmail()).orElseThrow(()->new BadRequestException(NOT_CORRECT_AUTH));

        if(!codeAuth.getCode().equals(findPassword.getCode()))throw new BadRequestException(NOT_CORRECT_CODE);

        User user = userAdaptor.findByUserName(findPassword.getEmail());

        user.setPassword(passwordEncoder.encode(findPassword.getModifyPassword()));

        user = userAdaptor.save(user);
    }
}

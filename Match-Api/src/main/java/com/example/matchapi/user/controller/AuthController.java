package com.example.matchapi.user.controller;

import com.example.matchapi.user.dto.UserReq;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchapi.user.service.AuthService;
import com.example.matchapi.user.helper.SmsHelper;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.annotation.DisableSecurity;
import com.example.matchcommon.exception.errorcode.MailSendErrorCode;
import com.example.matchcommon.exception.errorcode.OtherServerErrorCode;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchdomain.user.exception.*;
import com.example.matchcommon.reponse.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "01-Auth🔑", description = "회원가입, 로그인 토큰이 필요 없는 API 입니다.")
public class AuthController {
    private final AuthService authService;
    private final SmsHelper smsHelper;
    @Operation(summary = "kakao 코드 발급 후 토큰 생성용 개발용 API 입니다",description = "kakao 코드를 발급 할 수 있음")
    @GetMapping(value = "/kakao")
    @DisableSecurity
    public String kakaoOauthRedirect(@RequestParam String code) {
        log.info("카카오 로그인 액세스 토큰 발급");
        return "카카오 로그인 액세스 토큰 발급 완료, 액세스 토큰 :" + authService.getOauthToken(code,"").getAccess_token();

    }

    @ApiErrorCodeExample({UserSignUpErrorCode.class, OtherServerErrorCode.class})
    @Operation(summary= "01-02🔑 카카오 로그인" , description = "카카오 액세스 토큰 보내주기")
    @PostMapping(value="/kakao")
    @DisableSecurity
    public CommonResponse<UserRes.UserToken> kakaoLogIn(@RequestBody @Valid UserReq.SocialLoginToken socialLoginToken){
        log.info("01-02 카카오 로그인");
        return CommonResponse.onSuccess(authService.kakaoLogIn(socialLoginToken));
    }


    /*
    네이버 로그인 토큰 발급용
     */
    @ApiErrorCodeExample({UserSignUpErrorCode.class, OtherServerErrorCode.class})
    @GetMapping(value="/naver")
    @Operation(summary = "01-03-01🔑 web version API  naver 코드 발급 후 회원가입", description = "naver 코드를 발급 할 수 있음")
    @DisableSecurity
    public CommonResponse<UserRes.UserToken>  naverOauthRedirect(@RequestParam String code){
        log.info("01-03-01 웹 버전 naver 로그인,회원가입");
        return CommonResponse.onSuccess(authService.getNaverOauthToken(code));
    }

    @ApiErrorCodeExample({UserSignUpErrorCode.class, OtherServerErrorCode.class, RequestErrorCode.class})
    @Operation(summary= "01-03🔑 네이버 로그인" , description = "네이버 액세스 토큰 보내주기")
    @PostMapping(value="/naver")
    @DisableSecurity
    public CommonResponse<UserRes.UserToken> naverLogIn(@RequestBody @Valid UserReq.SocialLoginToken socialLoginToken){
        log.info("01-03 네이버 로그인,회원가입 API");
        return CommonResponse.onSuccess(authService.naverLogIn(socialLoginToken.getAccessToken()));
    }

    /*
    @PostMapping("/kakao/address")
    CommonResponse<KakaoUserAddressDto> getKakaoAddress(@RequestBody UserReq.SocialLoginToken socialLoginToken){
        return CommonResponse.onSuccess(authService.getKakaoAddress(socialLoginToken.getAccessToken()));
    }

    @PostMapping("/naver/address")
    CommonResponse<NaverAddressDto> getNaverAddress(@RequestBody UserReq.SocialLoginToken socialLoginToken){
        return CommonResponse.onSuccess(authService.getNaverAddress(socialLoginToken.getAccessToken()));
    }

     */



    @ApiErrorCodeExample(RequestErrorCode.class)
    @Operation(summary= "01-04🔑 회원 문자인증 요청", description = "회원 문자인증 용 API 입니다.")
    @PostMapping(value="/sms")
    @Deprecated
    @DisableSecurity
    public CommonResponse<UserRes.Sms> checkSms(@RequestBody @Valid UserReq.Sms sms){
        log.info("01-04 비회원 문자인증 = " +sms.getPhone());
        if(sms.getPhone().equals("01011111111")) return CommonResponse.onSuccess(new UserRes.Sms("111111"));
        String number = smsHelper.sendSms(sms.getPhone());
        return CommonResponse.onSuccess(new UserRes.Sms(number));
    }

    @ApiErrorCodeExample({UserNormalSignUpErrorCode.class, UserSignUpErrorCode.class, RequestErrorCode.class})
    @Operation(summary="01-05🔑 유저 회원가입", description= "회원가입 용 API 입니다.")
    @PostMapping(value="/user")
    @DisableSecurity
    public CommonResponse<UserRes.UserToken> signUpUser(@RequestBody @Valid UserReq.SignUpUser signUpUser){
        log.info("01-05 유저 회원가입 API");
        return CommonResponse.onSuccess(authService.signUpUser(signUpUser));
    }

    @ApiErrorCodeExample(RequestErrorCode.class)
    @Operation(summary="01-05-01🔑 유저 회원가입 이메일 검증용", description= "회원가입 용 API 입니다.")
    @PostMapping(value="/email")
    @DisableSecurity
    public CommonResponse<String> checkUserEmail(@RequestBody @Valid UserReq.UserEmail userEmail){
        if(userEmail.getEmail().equals("test1234@gmail.com")) return CommonResponse.onSuccess("이메일 사용 가능");
        log.info("01-05-01 유저 회원가입 이메일 검증"+userEmail.getEmail());
        authService.checkUserEmail(userEmail);
        return CommonResponse.onSuccess("이메일 사용 가능");
    }

    @ApiErrorCodeExample(RequestErrorCode.class)
    @Operation(summary="01-05-02🔑 유저 회원가입 전화번호 인증용", description= "회원가입 용 API 입니다.")
    @PostMapping(value="/phone")
    @DisableSecurity
    public CommonResponse<String> checkUserPhone(@RequestBody @Valid UserReq.UserPhone userPhone){
        log.info("01-05-01 유저 회원가입 전화번호 검증"+userPhone.getPhone());
        if(userPhone.getPhone().equals("01011111111")) return  CommonResponse.onSuccess("핸드폰 사용가능");
        authService.checkUserPhone(userPhone);
        return CommonResponse.onSuccess("핸드폰 사용가능");
    }

    @ApiErrorCodeExample({UserLoginErrorCode.class, RequestErrorCode.class})
    @Operation(summary="01-06🔑 유저 로그인", description= "회원가입 용 API 입니다.")
    @PostMapping(value="/logIn")
    @DisableSecurity
    public CommonResponse<UserRes.UserToken> logIn(@RequestBody @Valid UserReq.LogIn logIn){
        log.info("01-06 유저 로그인 회원가입 API "+logIn.getEmail());
        return CommonResponse.onSuccess(authService.logIn(logIn));
    }


    @Operation(summary="01-07🔑 유저 이메일 인증번호 보내기", description= "이메일 인증번호 보내기 API 입니다.")
    @ApiErrorCodeExample({MailSendErrorCode.class, UserNormalSignUpErrorCode.class})
    @GetMapping("/email")
    @DisableSecurity
    public CommonResponse<String> emailAuth(@RequestParam String email){
        if(email.equals("test1234@gmail.com")) return CommonResponse.onSuccess("이메일 사용 가능");
        authService.sendEmailMessage(email);
        return CommonResponse.onSuccess("메일 전송 성공");
    }

    @Operation(summary="01-08🔑 유저 이메일 인증번호 확인 API", description= "이메일 인증번호 확인 API 입니다.")
    @PostMapping("/check/email")
    @ApiErrorCodeExample(CodeAuthErrorCode.class)
    @DisableSecurity
    public CommonResponse<String> checkEmailAuth(@RequestBody UserReq.UserEmailAuth email){
        if(email.getEmail().equals("test1234@gmail.com"))return CommonResponse.onSuccess("메일 인증 성공");

        authService.checkUserEmailAuth(email);
        return CommonResponse.onSuccess("메일 인증 성공");
    }


    @ApiErrorCodeExample(RequestErrorCode.class)
    @Operation(summary= "01-09🔑 회원 문자인증 요청", description = "회원 문자인증 용 API 입니다.")
    @GetMapping(value="/phone")
    @DisableSecurity
    public CommonResponse<String> checkPhone(@RequestParam String phone){
        authService.sendPhone(phone);
        return CommonResponse.onSuccess("문자 전송 성공");
    }

    @Operation(summary="01-10🔑 유저 전화번호전화번호 인증번호 확인 API", description= "전화번호 인증번호 확인 API 입니다.")
    @PostMapping("/check/phone")
    @ApiErrorCodeExample(CodeAuthErrorCode.class)
    @DisableSecurity
    public CommonResponse<String> checkEmailAuth(@RequestBody UserReq.UserPhoneAuth phone){
        if(phone.getPhone().equals("01011111111")) return CommonResponse.onSuccess("핸드폰 인증 성공");

        authService.checkPhoneAuth(phone);
        return CommonResponse.onSuccess("핸드폰 인증 성공");
    }


    @Operation(summary="01-11🔑 애플로그인 API", description= "애플로그인 API 입니다. APPLE_SIGN_UP 에러 코드 발생 시 01-11-01 API 로 회원가입 요청")
    @PostMapping("/apple")
    @ApiErrorCodeExample({UserSignUpErrorCode.class, OtherServerErrorCode.class, RequestErrorCode.class, AppleLoginErrorCode.class})
    @DisableSecurity
    public CommonResponse<UserRes.UserToken> appleLogin(@RequestBody @Valid UserReq.SocialLoginToken socialLoginToken){
        return CommonResponse.onSuccess(authService.appleLogin(socialLoginToken));
    }

    @Operation(summary = "01-11-01 애플 회원가입🔑",description = "애플유저용 회원가입")
    @PostMapping("/apple/sign-up")
    @ApiErrorCodeExample({UserSignUpErrorCode.class, RequestErrorCode.class})
    @DisableSecurity
    public CommonResponse<UserRes.UserToken> appleSignUp(@RequestBody @Valid UserReq.AppleSignUp appleSignUp){
        return CommonResponse.onSuccess(authService.appleSignUp(appleSignUp));
    }

    @Operation(summary = "01-14🔑 비밀번호 찾기용 이메일 전송 이메일 전송 시 01-08 API 로 인증번호 확인 입니다.", description = "만료시간 5분")
    @PostMapping("/password/email")
    @ApiErrorCodeExample({UserSignUpErrorCode.class, SendEmailFindPassword.class})
    @DisableSecurity
    public CommonResponse<String> sendEmailPasswordFind(@RequestParam String email){
        if(email.equals("test1234@gmail.com")) return CommonResponse.onSuccess("이메일 사용 가능");
        authService.sendEmailPasswordFind(email);
        return CommonResponse.onSuccess("메일 인증 성공");
    }


    @Operation(summary = "01-13🔑 비밀번호 찾기", description = "여기서 또 한번 인증 코드를 받는 이유는 이중 인증을 위함 입니다. 변경은 5분안에 마무리 되야합니다.")
    @PostMapping("/password")
    @ApiErrorCodeExample({UserSignUpErrorCode.class, RequestErrorCode.class, CodeAuthErrorCode.class})
    @DisableSecurity
    public CommonResponse<String> modifyPassword(@RequestBody @Valid UserReq.FindPassword findPassword){
        authService.modifyPassword(findPassword);
        return CommonResponse.onSuccess("비밀번호 변경 성공");
    }


}

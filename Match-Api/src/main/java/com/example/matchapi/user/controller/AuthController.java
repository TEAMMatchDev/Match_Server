package com.example.matchapi.user.controller;

import com.example.matchapi.user.dto.UserReq;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchapi.user.service.AuthService;
import com.example.matchapi.user.utils.SmsHelper;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchinfrastructure.oauth.kakao.dto.KakaoUserAddressDto;
import com.example.matchinfrastructure.oauth.naver.dto.NaverAddressDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags = "01-Auth🔑")
public class AuthController {
    private final AuthService authService;
    private final SmsHelper smsHelper;
    private UserReq.UserEmail signUpUser;

    @ApiOperation(value = "kakao 코드 발급 후 토큰 생성용 개발용 API 입니다", notes = "kakao 코드를 발급 할 수 있음")
    @GetMapping(value = "/kakao")
    public String kakaoOauthRedirect(@RequestParam String code) {
        return "카카오 로그인 액세스 토큰 발급 완료, 액세스 토큰 :" + authService.getOauthToken(code,"").getAccess_token();

    }

    @ApiOperation(value= "01-02🔑 카카오 로그인" , notes = "카카오 액세스 토큰 보내주기")
    @PostMapping(value="/kakao")
    public CommonResponse<UserRes.UserToken> kakaoLogIn(@RequestBody @Valid UserReq.SocialLoginToken socialLoginToken){
        return CommonResponse.onSuccess(authService.kakaoLogIn(socialLoginToken));
    }


    /*
    네이버 로그인 토큰 발급용
     */
    @GetMapping(value="/naver")
    @ApiOperation(value = "naver 코드 발급 후 토큰 생성용 개발용 API 입니다", notes = "naver 코드를 발급 할 수 있음")
    public String naverOauthRedirect(@RequestParam String code){
        return authService.getNaverOauthToken(code);
    }

    @ApiOperation(value= "01-03🔑 네이버 로그인" , notes = "네이버 액세스 토큰 보내주기")
    @PostMapping(value="/naver")
    public CommonResponse<UserRes.UserToken> naverLogIn(@RequestBody @Valid UserReq.SocialLoginToken socialLoginToken){
        return CommonResponse.onSuccess(authService.naverLogIn(socialLoginToken));
    }

    @PostMapping("/kakao/address")
    CommonResponse<KakaoUserAddressDto> getKakaoAddress(@RequestBody UserReq.SocialLoginToken socialLoginToken){
        return CommonResponse.onSuccess(authService.getKakaoAddress(socialLoginToken.getAccessToken()));
    }

    @PostMapping("/naver/address")
    CommonResponse<NaverAddressDto> getNaverAddress(@RequestBody UserReq.SocialLoginToken socialLoginToken){
        return CommonResponse.onSuccess(authService.getNaverAddress(socialLoginToken.getAccessToken()));
    }


    @ApiOperation(value= "01-04🔑 회원 문자인증 요청", notes = "회원 문자인증 용 API 입니다.")
    @PostMapping(value="/sms")
    public CommonResponse<UserRes.Sms> checkSms(@RequestBody UserReq.Sms sms){
        String number = smsHelper.sendSms(sms.getPhone());
        return CommonResponse.onSuccess(new UserRes.Sms(number));
    }

    @ApiOperation(value="01-05🔑 유저 회원가입", notes= "회원가입 용 API 입니다.")
    @PostMapping(value="/user")
    public CommonResponse<UserRes.UserToken> signUpUser(@RequestBody @Valid UserReq.SignUpUser signUpUser){
        return CommonResponse.onSuccess(authService.signUpUser(signUpUser));
    }

    @ApiOperation(value="01-05-01🔑 유저 회원가입 이메일 검증용", notes= "회원가입 용 API 입니다.")
    @PostMapping(value="/email")
    public CommonResponse<String> checkUserEmail(@RequestBody @Valid UserReq.UserEmail userEmail){
        authService.checkUserEmail(userEmail);
        return CommonResponse.onSuccess("이메일 사용 가능");
    }

    @ApiOperation(value="01-05-02🔑 유저 회원가입 전화번호 인증용", notes= "회원가입 용 API 입니다.")
    @PostMapping(value="/phone")
    public CommonResponse<String> checkUserPhone(@RequestBody @Valid UserReq.UserPhone userPhone){
        authService.checkUserPhone(userPhone);
        return CommonResponse.onSuccess("핸드폰 사용가능");
    }









}

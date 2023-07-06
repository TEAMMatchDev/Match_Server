package com.example.matchapi.user.controller;

import com.example.matchapi.user.dto.UserReq;
import com.example.matchapi.user.service.AuthService;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchinfrastructure.oauth.dto.KakaoLoginTokenRes;
import com.example.matchinfrastructure.oauth.dto.KakaoUserInfoDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags = "01-Auth🔑")
public class AuthController {
    private final AuthService authService;

    @ApiOperation(value = "kakao 코드 발급 후 토큰 생성용 개발용 API 입니다", notes = "kakao 코드를 발급 할 수 있음")
    @GetMapping(value = "/kakao")
    public String kakaoOauthRedirect(@RequestParam String code) {
        return "카카오 로그인 액세스 토큰 발급 완료, 액세스 토큰 :" + authService.getOauthToken(code,"").getAccess_token();

    }

    @ApiOperation(value= "01-02🔑 카카오 로그인" , notes = "카카오 액세스 토큰 보내주기")
    @PostMapping(value="/kakao")
    public CommonResponse<KakaoUserInfoDto> kakaoLogIn(@RequestBody UserReq.SocialLoginToken socialLoginToken){
        return CommonResponse.onSuccess(authService.kakaoLogIn(socialLoginToken));
    }





}

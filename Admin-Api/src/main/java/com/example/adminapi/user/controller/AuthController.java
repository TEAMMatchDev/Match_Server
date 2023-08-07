package com.example.adminapi.user.controller;

import com.example.adminapi.user.dto.UserReq;
import com.example.adminapi.user.dto.UserRes;
import com.example.adminapi.user.service.AuthService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.user.exception.AdminLoginErrorCode;
import com.example.matchdomain.user.exception.UserLoginErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Slf4j
@Tag(name = "01-ADMIN Auth🔑", description = "ADMIN 로그인 API 입니다.")
public class AuthController {
    private final AuthService authService;
    @ApiErrorCodeExample({UserLoginErrorCode.class, RequestErrorCode.class, AdminLoginErrorCode.class})
    @Operation(summary="ADMIN 01-01🔑 유저 로그인", description= "로그인 API 입니다.")
    @PostMapping(value="/logIn")
    public CommonResponse<UserRes.UserToken> logIn(@RequestBody @Valid UserReq.LogIn logIn){
        log.info("ADMIN 관리자 로그인 API "+logIn.getEmail());
        return CommonResponse.onSuccess(authService.logIn(logIn));
    }

}

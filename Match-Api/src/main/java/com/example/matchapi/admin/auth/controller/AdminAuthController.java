package com.example.matchapi.admin.auth.controller;

import com.example.matchapi.user.dto.UserReq;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchapi.user.service.AuthService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.annotation.DisableSecurity;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.user.exception.AdminLoginErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/admin/auth")
@RequiredArgsConstructor
@RestController
@Slf4j
@Tag(name = "ADMIN-00-00🔑  관리자 로그인")
public class AdminAuthController {
    private final AuthService authService;
    @ApiErrorCodeExample({AdminLoginErrorCode.class, RequestErrorCode.class})
    @DisableSecurity
    @Operation(summary="ADMIN-00-01🔑 관리자 로그인", description= "회원가입 용 API 입니다.")
    @PostMapping(value="/logIn")
    public CommonResponse<UserRes.UserToken> logIn(@RequestBody @Valid UserReq.LogIn logIn){
        log.info("00-01 유저 로그인 API "+logIn.getEmail());
        return CommonResponse.onSuccess(authService.adminLogIn(logIn));
    }
}

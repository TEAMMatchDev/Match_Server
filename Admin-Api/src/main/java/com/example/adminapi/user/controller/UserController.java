package com.example.adminapi.user.controller;

import com.example.adminapi.user.dto.UserRes;
import com.example.adminapi.user.service.UserService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "02-ADMIN User🔑 유저 관련 API 입니다.", description = "ADMIN 유저 관 API 입니다.")
public class UserController {
    private final UserService userService;
    @GetMapping("")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "02-01💻 윺저 가입 현황파악 API.",description = "프로젝트 리스트 조회 API 입니다.")
    public CommonResponse<UserRes.SignUpInfo> getUserSingUpInfo(){
        UserRes.SignUpInfo signUpInfo = userService.getUserSignUpInfo();
        return CommonResponse.onSuccess(signUpInfo);
    }

}

package com.example.matchapi.user.controller;

import com.example.matchapi.user.dto.UserRes;
import com.example.matchapi.user.service.UserService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "02-User👤",description = "유저 관련 API 입니다.")
public class UserController {
    private final UserService userService;

    @Operation(summary= "02-01👤 마이페이지 전체 조회",description = "마이페이지 전체 조회입니다.")
    @GetMapping("")
    public CommonResponse<UserRes.MyPage> getMyPage(@Parameter(hidden = true)
                                                        @AuthenticationPrincipal User user){
        log.info("02-01 마이페이지 전체조회 userId : " + user.getId());
        return CommonResponse.onSuccess(userService.getMyPage(user));
    }

    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "02-02👤 MYPage 편집화면 내 정보 조회", description = "마이페이지 편집을 위한 조회 화면입니다.")
    @GetMapping(value = "/my-page/edit")
    public CommonResponse<UserRes.EditMyPage> getEditMyPage(@Parameter(hidden = true)
                                                         @AuthenticationPrincipal User user){
        log.info("02-02 마이페이지 편집화면 조회 userId : " + user.getId());
        return CommonResponse.onSuccess(userService.getEditMyPage(user));
    }
}

package com.example.matchapi.user.controller;

import com.example.matchapi.user.dto.UserRes;
import com.example.matchapi.user.service.UserService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.exception.errorcode.UserAuthErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "02-User👤")
public class UserController {
    private final UserService userService;

    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "02-01👤 MYPage 조회", description = "마이페이지 편집을 위한 조회 화면입니다.")
    @GetMapping(value = "/my-page")
    public CommonResponse< UserRes.MyPage> getMyPage(@Parameter(hidden = true)
                                                         @AuthenticationPrincipal User user){
        return CommonResponse.onSuccess(userService.getMyPage(user));
    }
}

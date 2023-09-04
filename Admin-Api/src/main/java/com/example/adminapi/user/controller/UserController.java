package com.example.adminapi.user.controller;

import com.example.adminapi.user.dto.UserRes;
import com.example.adminapi.user.service.UserService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "02-ADMIN User🔑 유저 관련 API 입니다.", description = "ADMIN 유저 관 API 입니다.")
public class UserController {
    private final UserService userService;
    @GetMapping("/info")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "02-01💻 윺저 가입 현황파악 API.",description = "프로젝트 리스트 조회 API 입니다.")
    public CommonResponse<UserRes.SignUpInfo> getUserSingUpInfo(){
        UserRes.SignUpInfo signUpInfo = userService.getUserSignUpInfo();
        return CommonResponse.onSuccess(signUpInfo);
    }

    @GetMapping("")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "02-01💻 유저 가입 현황파악 리스트조회 API.",description = "유저 리스트 조회 API 입니다.")
    public CommonResponse<PageResponse<List<UserRes.UserList>>> getUserList(
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") @Min(value = 0) int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "10") int size
    ){
        PageResponse<List<UserRes.UserList>> userList = userService.getUserList(page, size);
        return CommonResponse.onSuccess(userList);
    }

}

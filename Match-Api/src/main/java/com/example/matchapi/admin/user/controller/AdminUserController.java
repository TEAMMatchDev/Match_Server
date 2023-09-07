package com.example.matchapi.admin.user.controller;

import com.example.matchapi.user.dto.UserRes;
import com.example.matchapi.user.service.UserService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Tag(name = "ADMIN-02 User👤 관리자 유저 관련 API 입니다.", description = "ADMIN 유저 관련 API 입니다.")
public class AdminUserController {
    private final UserService userService;
    @GetMapping("/info")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "ADMIN-02-01👤 유저저 가입 현황파악 API.",description = "프로젝트 리스트 조회 API 입니다.")
    public CommonResponse<UserRes.SignUpInfo> getUserSingUpInfo(){
        UserRes.SignUpInfo signUpInfo = userService.getUserSignUpInfo();
        return CommonResponse.onSuccess(signUpInfo);
    }

    @GetMapping("")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "ADMIN-02-02👤 유저 가입 현황파악 리스트조회 API.",description = "유저 리스트 조회 API 입니다.")
    public CommonResponse<PageResponse<List<UserRes.UserList>>> getUserList(
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") @Min(value = 0) int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) String content
            ){
        PageResponse<List<UserRes.UserList>> userList = userService.getUserList(page, size, status, content);
        return CommonResponse.onSuccess(userList);
    }

}

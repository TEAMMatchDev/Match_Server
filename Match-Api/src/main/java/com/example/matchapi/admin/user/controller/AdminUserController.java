package com.example.matchapi.admin.user.controller;

import com.example.matchapi.donation.service.DonationService;
import com.example.matchapi.user.converter.UserConverter;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchapi.user.service.UserService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Tag(name = "ADMIN-02 User👤 관리자 유저 관련 API 입니다.", description = "ADMIN 유저 관련 API 입니다.")
public class AdminUserController {
    private final UserService userService;
    private final DonationService donationService;
    private final UserConverter userConverter;
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

    @GetMapping("/{userId}")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "ADMIN-02-03👤 유저 상세 조회 API.",description = "유저 상세 조회 API 입니다.")
    public CommonResponse<UserRes.UserAdminDetail> getUserDetail(@PathVariable Long userId){
        UserRes.UserAdminDetail userAdminDetail = userService.getUserAdminDetail(userId);
        return CommonResponse.onSuccess(userAdminDetail);
    }

    @GetMapping("/flame/{userId}")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "ADMIN-02-04 유저 불꽃이 생성기록 조회" ,description = "유저 불꽃이 기록 조회")
    public CommonResponse<PageResponse<List<UserRes.UserFlameListDto>>> getUserFlameList(@PathVariable Long userId,
                                                                     @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") @Min(value = 0) int page,
                                                                     @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "10") int size){
        User user = userService.findByUserId(userId);
        Page<DonationUser> donationUsers = donationService.findByUserId(user, page, size);

        return CommonResponse.onSuccess(new PageResponse<>(donationUsers.isLast(), donationUsers.getTotalElements(), userConverter.convertToFlameList(donationUsers.getContent())));
    }
}

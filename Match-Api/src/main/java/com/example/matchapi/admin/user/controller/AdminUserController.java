package com.example.matchapi.admin.user.controller;

import com.example.matchapi.admin.user.service.AdminUserService;
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
import com.example.matchdomain.user.entity.enums.Gender;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Tag(name = "ADMIN-02 User👤 관리자 유저 관련 API 입니다.", description = "ADMIN 유저 관련 API 입니다.")
public class AdminUserController {
    private final AdminUserService adminUserService;
    private final DonationService donationService;
    private final UserConverter userConverter;
    @GetMapping("/info")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "ADMIN-02-01👤 유저저 가입 현황파악 API.",description = "프로젝트 리스트 조회 API 입니다.")
    public CommonResponse<UserRes.SignUpInfo> getUserSingUpInfo(){
        UserRes.SignUpInfo signUpInfo = adminUserService.getUserSignUpInfo();
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
        PageResponse<List<UserRes.UserList>> userList = adminUserService.getUserList(page, size, status, content);
        return CommonResponse.onSuccess(userList);
    }

    @GetMapping("/{userId}")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "ADMIN-02-03👤 유저 상세 조회 API.",description = "유저 상세 조회 API 입니다.")
    public CommonResponse<UserRes.UserAdminDetail> getUserDetail(@PathVariable Long userId){
        UserRes.UserAdminDetail userAdminDetail = adminUserService.getUserAdminDetail(userId);
        return CommonResponse.onSuccess(userAdminDetail);
    }

    @GetMapping("/flame/{userId}")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "ADMIN-02-04 유저 불꽃이 생성기록 조회" ,description = "유저 불꽃이 기록 조회")
    public CommonResponse<PageResponse<List<UserRes.UserFlameListDto>>> getUserFlameList(@PathVariable Long userId,
                                                                     @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
                                                                     @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "10") int size){
        User user = adminUserService.findByUserId(userId);
        Page<DonationUser> donationUsers = donationService.findByUserId(user, page, size);

        return CommonResponse.onSuccess(new PageResponse<>(donationUsers.isLast(), donationUsers.getTotalElements(), userConverter.convertToFlameList(donationUsers.getContent())));
    }

    @DeleteMapping("/{userId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class})
    @Operation(summary = "ADMIN-02-05👤 유저 삭제 API.",description = "유저 삭제 API 입니다.")
    public CommonResponse<UserRes.UserDelete> deleteUser(@PathVariable Long userId){
        User user = adminUserService.findByUserId(userId);
        adminUserService.unActivateUser(user);
        return CommonResponse.onSuccess(new UserRes.UserDelete(userId));
    }

    @PatchMapping("/nickname/{userId}")
    @Operation(summary = "ADMIN-02-06👤 유저 닉네임 수정 API.",description = "유저 닉네임 수정 API 입니다.")
    public CommonResponse<String> updateNickname(@PathVariable Long userId, @RequestParam String nickname){
        adminUserService.updateNickname(userId, nickname);
        return CommonResponse.onSuccess("닉네임 수정 성공");
    }

    @PatchMapping("/birth/{userId}")
    @Operation(summary = "ADMIN-02-07👤 유저 생일 수정 API.",description = "유저 생일 수정 API 입니다.")
    public CommonResponse<String> updateBirth(@PathVariable Long userId, @RequestParam LocalDate birth){
        adminUserService.updateBirth(userId, birth);
        return CommonResponse.onSuccess("생일 수정 성공");
    }

    @PatchMapping("/phone/{userId}")
    @Operation(summary = "ADMIN-02-08👤 유저 전화번호 수정 API.",description = "유저 전화번호 수정 API 입니다.")
    public CommonResponse<String> updatePhone(@PathVariable Long userId, @RequestParam String phone){
        adminUserService.updatePhone(userId, phone);
        return CommonResponse.onSuccess("전화번호 수정 성공");
    }

    @PatchMapping("/email/{userId}")
    @Operation(summary = "ADMIN-02-09 유저 이메일 수정 API" , description = "유저 이메일 수정 API")
    public CommonResponse<String> updateEmail(@PathVariable Long userId, @RequestParam String email){
        adminUserService.updateEmail(userId, email);
        return CommonResponse.onSuccess("이메일 수정 성공");
    }

    @PatchMapping("/gender/{userId}")
    @Operation(summary = "ADMIN-02-10 유저 성별 수정 API" , description = "유저 성별 수정 API")
    public CommonResponse<String> updateGender(@PathVariable Long userId, @RequestParam Gender gender){
        adminUserService.updateGender(userId, gender);
        return CommonResponse.onSuccess("성별 수정 성공");
    }
}

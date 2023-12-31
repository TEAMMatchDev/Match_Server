package com.example.matchapi.user.controller;

import com.example.matchapi.common.model.AlarmType;
import com.example.matchapi.common.security.JwtService;
import com.example.matchapi.donation.service.DonationService;
import com.example.matchapi.project.converter.ProjectConverter;
import com.example.matchapi.project.service.ProjectService;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchapi.user.dto.UserReq;
import com.example.matchapi.user.service.RefreshTokenService;
import com.example.matchapi.user.service.UserFcmService;
import com.example.matchapi.user.service.UserService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.annotation.DisableSecurity;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.project.entity.enums.ProjectStatus;
import com.example.matchdomain.redis.entity.RefreshToken;
import com.example.matchdomain.redis.repository.RefreshTokenRepository;
import com.example.matchdomain.user.entity.enums.SocialType;
import com.example.matchdomain.user.exception.*;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.example.matchdomain.user.exception.DeleteUserErrorCode.APPLE_USER_NOT_API;
import static com.example.matchdomain.user.exception.UserAuthErrorCode.INVALID_REFRESH_TOKEN;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "02-User👤",description = "유저 관련 API 입니다.")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserFcmService userFcmService;
    private final ProjectService projectService;
    private final DonationService donationService;
    private final ProjectConverter projectConverter;

    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Deprecated
    @Operation(summary = "02-02👤 MYPage 편집화면 내 정보 조회", description = "마이페이지 편집을 위한 조회 화면입니다.")
    @GetMapping(value = "/my-page/edit")
    public CommonResponse<UserRes.EditMyPage> getEditMyPage(@Parameter(hidden = true)
                                                         @AuthenticationPrincipal User user){
        log.info("02-02 마이페이지 편집화면 조회 userId : " + user.getId());
        return CommonResponse.onSuccess(userService.getEditMyPage(user));
    }

    @ApiErrorCodeExample({UserAuthErrorCode.class, RequestErrorCode.class})
    @Deprecated
    @Operation(summary = "02-02👤 MYPage 편집화면 내 정보 수정", description = "마이페이지 편집을 위한 API 입니다.")
    @PatchMapping("/my-page/edit")
    public CommonResponse<String> editMyPage(@Parameter(hidden = true)
                                                 @AuthenticationPrincipal User user, @RequestBody UserReq.EditMyPage editMyPage){
        return CommonResponse.onSuccess("성공");
    }


    @Operation(summary = "02-04 로그아웃 👤", description = "로그아웃 요청 API")
    @GetMapping("/logout")
    @Transactional
    public CommonResponse<String> logOut(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                         @Parameter(description = "디바이스 아이디") @RequestParam(value = "DEVICE_ID", required = true) String deviceId){
        Long userId = user.getId();
        jwtService.logOut(userId);
        userFcmService.deleteFcmToken(userId, deviceId);
        return CommonResponse.onSuccess("로그아웃 성공");
    }

    @Operation(summary = "02-05 토큰 재발급 👤", description = "액세스 토큰 만료시 재발급 요청 하는 API X-REFRESH-TOKEN 을 헤더에 담아서 보내주세요, accessToken 은 보내지 않습니다.")
    @PostMapping("/refresh")
    @DisableSecurity
    public CommonResponse<UserRes.ReIssueToken> reIssueToken(
            @Parameter(description = "리프레쉬 토큰", required = true, in = ParameterIn.HEADER, name = "X-REFRESH-TOKEN", schema = @Schema(type = "string")) @RequestHeader("X-REFRESH-TOKEN") String refreshToken
    ){
        Long userId=jwtService.getUserIdByRefreshToken(refreshToken);

        RefreshToken redisRefreshToken= refreshTokenService.findRefreshToken(userId);

        if(!redisRefreshToken.getToken().equals(refreshToken)) throw new BadRequestException(INVALID_REFRESH_TOKEN);

        return CommonResponse.onSuccess(new UserRes.ReIssueToken(jwtService.createToken(userId)));

    }

    @Operation(summary= "02-01👤 마이페이지 전체 조회",description = "마이페이지 전체 조회입니다.")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @GetMapping("")
    public CommonResponse<UserRes.MyPage> getMyPage(@Parameter(hidden = true)
                                                    @AuthenticationPrincipal User user){
        log.info("02-01 마이페이지 전체조회 userId : " + user.getId());
        Long projectAttentionCnt = projectService.getProjectAttentionCnt(user.getId());
        List<RegularPayment> regularPayments = donationService.findByUser(user);
        return CommonResponse.onSuccess(projectConverter.getMyPage(regularPayments, projectAttentionCnt, user.getNickname()));
    }

    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @GetMapping("/profile")
    @Operation(summary= "02-02👤 프로필 조회",description = "프로필 조회입니다.")
    public CommonResponse<UserRes.Profile> getProfile(
            @Parameter(hidden = true)
            @AuthenticationPrincipal User user
    ){
        return CommonResponse.onSuccess(userService.getProfile(user));
    }

    @Operation(summary = "02-06 프로필 편집 👤 FRAME MY",description = "이미지 파일 변경할 경우 multipart 에 넣어주시고, 이미지 변경 안할 시 multipart null 값으로 보내주세요 아이디는 기존 아이디값+변경할 아이디값 둘중 하나 보내시면 됩니다")
    @PatchMapping(value =  "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<String> modifyUserProfile(
            @RequestParam(value = "name", required = false) String name,
            @RequestPart(value = "multipartFile", required = false) MultipartFile multipartFile,
            @AuthenticationPrincipal User user){
        userService.modifyUserProfile(user, new UserReq.ModifyProfile(name, multipartFile));
        return CommonResponse.onSuccess("변경 성공");
    }



    @Operation(summary = "02-07 유저 FCM 토큰 생성후 전송 👤",description = "유저 FCM 토큰과 deviceId 를 보내주시면 됩니다.")
    @PostMapping("/fcm")
    public CommonResponse<String> saveFcmToken(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @RequestBody UserReq.FcmToken token
    ){
        userFcmService.saveFcmToken(user, token);
        return CommonResponse.onSuccess("저장 성공");
    }

    @Operation(summary = "02-08 휴대폰번호 변경 👤",description = "휴대폰 번호 변경.")
    @PostMapping("/phone")
    @ApiErrorCodeExample({UserAuthErrorCode.class, ModifyPhoneErrorCode.class})
    public CommonResponse<String> modifyPhoneNumber(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @RequestBody UserReq.ModifyPhone phone
    ){
        userService.modifyPhoneNumber(user, phone);
        return CommonResponse.onSuccess("변경 성공");
    }

    @Operation(summary = "02-08 이메일 변경 👤",description = "이메일 변경.")
    @PostMapping("/email")
    @ApiErrorCodeExample({UserAuthErrorCode.class, ModifyEmailCode.class})
    public CommonResponse<String> modifyEmail(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @RequestBody UserReq.ModifyEmail email
    ){
        userService.modifyEmail(user, email);
        return CommonResponse.onSuccess("변경 성공");
    }

    @Operation(summary = "02-09 알람 동의 항목 조회 👤",description = "알람 동의 항목 조회 입니다 ACTIVE 필드와 INACTIVE 필드가 있습니다.")
    @GetMapping("/alarm")
    @ApiErrorCodeExample({UserAuthErrorCode.class})
    public CommonResponse<UserRes.AlarmAgreeList> getAlarmAgreeList(@AuthenticationPrincipal User user){
        return CommonResponse.onSuccess(userService.getAlarmAgreeList(user));
    }

    @Operation(summary = "02-10 알람 동의 항목 수정 👤" , description = "알람 동의 항목 수정")
    @PatchMapping("/alarm")
    @ApiErrorCodeExample({UserAuthErrorCode.class})
    public CommonResponse<UserRes.AlarmAgreeList> patchAlarmAgree(@AuthenticationPrincipal User user,
                                                                    @RequestParam AlarmType alarmType){
        return CommonResponse.onSuccess(userService.patchAlarm(user, alarmType));
    }
    @Operation(summary = "02-11 애플유저 결제화면 추가 정보 POST 👤" , description = "애플 유저 결제 화면 추가정보 POST")
    @PostMapping("/apple")
    @ApiErrorCodeExample({UserAuthErrorCode.class, CheckUserPhoneErrorCode.class})
    public CommonResponse<String> postAppleUserInfo(@AuthenticationPrincipal User user,
                                                    @Valid @RequestBody UserReq.AppleUserInfo appleUserInfo){
        log.info("02-11 애플 유저 결제화면 추가 정보 POST API");
        userService.postAppleUserInfo(user, appleUserInfo);
        return CommonResponse.onSuccess("성공");
    }

    @Operation(summary = "02-12 유저 탈퇴 API")
    @DeleteMapping("")
    @ApiErrorCodeExample({UserAuthErrorCode.class, DeleteUserErrorCode.class})
    public CommonResponse<String> deleteUserInfo(@AuthenticationPrincipal User user){
        log.info("02-12 유저 탈퇴 API userId : " + user.getId());
        if(user.getSocialType().equals(SocialType.APPLE)){
            throw new BadRequestException(APPLE_USER_NOT_API);
        }
        userService.deleteUserInfo(user);
        return CommonResponse.onSuccess("탈퇴 성공");
    }

    @Operation(summary = "02-13 애플 유저 탈퇴 API")
    @DeleteMapping("/apple")
    @ApiErrorCodeExample({UserAuthErrorCode.class})
    public CommonResponse<String> deleteAppleUserInfo(@AuthenticationPrincipal User user,
                                                      @Valid @RequestBody UserReq.AppleCode appleCode){
        log.info("02-13 애플 유저 탈퇴 code : " + appleCode.getCode());
        userService.deleteAppleUserInfo(user, appleCode);
        return CommonResponse.onSuccess("탈퇴 성공");
    }

}

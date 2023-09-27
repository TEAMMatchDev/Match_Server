package com.example.matchapi.user.controller;

import com.example.matchapi.security.JwtService;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchapi.user.dto.UserReq;
import com.example.matchapi.user.service.UserService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchdomain.redis.entity.RefreshToken;
import com.example.matchdomain.redis.repository.RefreshTokenRepository;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

import static com.example.matchdomain.user.exception.UserAuthErrorCode.INVALID_REFRESH_TOKEN;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "02-User👤",description = "유저 관련 API 입니다.")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    /*
    @Deprecated
    @Operation(summary= "02-01👤 마이페이지 전체 조회",description = "마이페이지 전체 조회입니다.")
    @GetMapping("")
    public CommonResponse<UserRes.MyPage> getMyPage(@Parameter(hidden = true)
                                                        @AuthenticationPrincipal User user){
        log.info("02-01 마이페이지 전체조회 userId : " + user.getId());
        return CommonResponse.onSuccess(userService.getMyPage(user));
    }

     */

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
    @ResponseBody
    @GetMapping("/logout")
    public CommonResponse<String> logOut(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                         @Parameter(description = "디바이스 아이디", required = true, in = ParameterIn.HEADER, name = "DEVICE_ID", schema = @Schema(type = "string")) @RequestHeader("DEVICE_ID") String deviceId){
        log.info("api = logout 02-03");
        Long userId = user.getId();

        jwtService.logOut(userId);
        userService.deleteFcmToken(userId, deviceId);
        return CommonResponse.onSuccess("로그아웃 성공");
    }

    @Operation(summary = "02-05 토큰 재발급 👤", description = "액세스 토큰 만료시 재발급 요청 하는 API X-REFRESH-TOKEN 을 헤더에 담아서 보내주세요, accessToken 은 보내지 않습니다.")
    @ResponseBody
    @PostMapping("/refresh")
    public CommonResponse<UserRes.ReIssueToken> reIssueToken(
            @Parameter(description = "리프레쉬 토큰", required = true, in = ParameterIn.HEADER, name = "X-REFRESH-TOKEN", schema = @Schema(type = "string")) @RequestHeader("X-REFRESH-TOKEN") String refreshToken
    ){
        log.info("reIssue-token : "+refreshToken);
        log.info("api = reIssue-token 02-04");
        Long userId=jwtService.getUserIdByRefreshToken(refreshToken);
        RefreshToken redisRefreshToken= refreshTokenRepository.findById(String.valueOf(userId)).orElseThrow(()-> new BadRequestException(INVALID_REFRESH_TOKEN));

        if(!redisRefreshToken.getToken().equals(refreshToken)) throw new BadRequestException(INVALID_REFRESH_TOKEN);

        UserRes.ReIssueToken tokenRes=new UserRes.ReIssueToken(jwtService.createToken(userId));

        return CommonResponse.onSuccess(tokenRes);

    }

    @Operation(summary= "02-01👤 마이페이지 전체 조회",description = "마이페이지 전체 조회입니다.")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @GetMapping("")
    public CommonResponse<UserRes.MyPage> getMyPage(@Parameter(hidden = true)
                                                    @AuthenticationPrincipal User user){
        log.info("02-01 마이페이지 전체조회 userId : " + user.getId());
        return CommonResponse.onSuccess(userService.getMyPage(user));
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
    @PatchMapping("/profile")
    public CommonResponse<String> modifyUserProfile(@ModelAttribute UserReq.ModifyProfile modifyProfile, @Parameter(hidden = true) @AuthenticationPrincipal User user) throws IOException {
        userService.modifyUserProfile(user, modifyProfile);
        return CommonResponse.onSuccess("변경 성공");
    }



    @Operation(summary = "02-07 유저 FCM 토큰 생성후 전송 👤",description = "유저 FCM 토큰과 deviceId 를 보내주시면 됩니다.")
    @PostMapping("/fcm")
    public CommonResponse<String> saveFcmToken(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @RequestBody UserReq.FcmToken token
    ){
        userService.saveFcmToken(user, token);
        return CommonResponse.onSuccess("저장 성공");
    }

    @Operation(summary = "02-08 휴대폰번호 변경 👤",description = "휴대폰 번호 변경.")
    @PostMapping("/phone")
    public CommonResponse<String> modifyPhoneNumber(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @RequestBody UserReq.ModifyPhone phone
    ){
        userService.modifyPhoneNumber(user, phone);
        return CommonResponse.onSuccess("저장 성공");
    }



}

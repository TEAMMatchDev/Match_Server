package com.example.matchapi.notification.controller;

import com.example.matchapi.notification.dto.NotificationRes;
import com.example.matchapi.notification.service.NotificationService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.notification.exception.GetNotificationErrorCode;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
@Tag(name = "10-Notification🔔 알림")
public class NotificationController {
    private final NotificationService notificationService;
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "10-01 알림 리스트 조회")
    @GetMapping("")
    public CommonResponse<PageResponse<NotificationRes.NotificationListInfo>> getNotificationList(
            @AuthenticationPrincipal User user,
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "10") int size
    ){
        return CommonResponse.onSuccess(notificationService.getNotificationList(user, page, size));
    }

    @ApiErrorCodeExample({UserAuthErrorCode.class, GetNotificationErrorCode.class})
    @Operation(summary = "10-02 알림 상세 조회")
    @GetMapping("/{notificationId}")
    public CommonResponse<NotificationRes.NotificationDetail> getNotificationDetail(@PathVariable Long notificationId){
        return CommonResponse.onSuccess(notificationService.getNotificationDetail(notificationId));
    }

}

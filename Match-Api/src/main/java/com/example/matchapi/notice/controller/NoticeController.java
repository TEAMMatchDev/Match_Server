package com.example.matchapi.notice.controller;

import com.example.matchapi.notice.dto.NoticeRes;
import com.example.matchapi.notice.service.NoticeService;
import com.example.matchapi.notification.dto.NotificationRes;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.notice.exception.GetNoticeErrorCode;
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
@RequestMapping("/notices")
@Tag(name = "11-Notice📝 공지사항 API")
public class NoticeController {
    private final NoticeService noticeService;

    @Operation(summary = "11-01 공지사항 리스트 조회")
    @GetMapping("")
    public CommonResponse<PageResponse<List<NoticeRes.NoticeList>>> getNotificationList(
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "10") int size
    ){
        return CommonResponse.onSuccess(noticeService.getNoticeList(page, size));
    }

    @Operation(summary = "11-02 공지사항 상세 조회")
    @GetMapping("/{noticeId}")
    @ApiErrorCodeExample({GetNoticeErrorCode.class, UserAuthErrorCode.class})
    public CommonResponse<NoticeRes.NoticeDetail> getNoticeDetail(@PathVariable Long noticeId){
        return CommonResponse.onSuccess(noticeService.getNoticeDetail(noticeId));
    }
}

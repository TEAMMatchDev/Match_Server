package com.example.matchapi.admin.notice.controller;

import com.example.matchapi.admin.event.dto.EventUploadReq;
import com.example.matchapi.admin.notice.dto.NoticeUploadReq;
import com.example.matchapi.admin.notice.mapper.AdminNoticeMapper;
import com.example.matchapi.admin.notice.service.AdminNoticeService;
import com.example.matchapi.common.model.ContentsList;
import com.example.matchapi.notice.dto.NoticeRes;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.notice.entity.Notice;
import com.example.matchdomain.notice.entity.NoticeContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/notices")
@Tag(name = "ADMIN-10-Notice📝 관리자 공지사항 관리 API")
public class AdminNoticeController {
    private final AdminNoticeService adminNoticeService;
    private final AdminNoticeMapper mapper = AdminNoticeMapper.INSTANCE;
    @PostMapping("")
    @Operation(summary = "ADMIN-10-01 공지사항 업로드")
    public CommonResponse<String> uploadNoticeList(@Valid @RequestBody NoticeUploadReq noticeUploadReq){
        adminNoticeService.uploadNoticeList(mapper.toEntityNoticeContents(noticeUploadReq.getContentsList()), mapper.toEntityNotice(noticeUploadReq));
        return CommonResponse.onSuccess("업로드 성공");
    }

    @GetMapping("")
    @Operation(summary = "ADMIN-10-02 공지사항 조회")
    public CommonResponse<PageResponse<List<NoticeRes.NoticeList>>> getNoticeList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size){
        return CommonResponse.onSuccess(adminNoticeService.getNoticeList(page,size));
    }

    @DeleteMapping("/{noticeId}")
    @Operation(summary = "ADMIN-10-02 공지사항 삭제")
    public CommonResponse<String> deleteNotice(@PathVariable Long noticeId){
        adminNoticeService.deleteNotice(noticeId);
        return CommonResponse.onSuccess("삭제 성공");
    }
}

package com.example.matchapi.admin.notice.controller;

import com.example.matchapi.admin.event.dto.EventUploadReq;
import com.example.matchapi.admin.notice.dto.NoticeUploadReq;
import com.example.matchapi.admin.notice.mapper.AdminNoticeMapper;
import com.example.matchapi.admin.notice.service.AdminNoticeService;
import com.example.matchapi.common.model.ContentsList;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.notice.entity.Notice;
import com.example.matchdomain.notice.entity.NoticeContent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/notices")
public class AdminNoticeController {
    private final AdminNoticeService adminNoticeService;
    private final AdminNoticeMapper mapper = AdminNoticeMapper.INSTANCE;
    @PostMapping("")
    public CommonResponse<String> uploadNoticeList(@Valid @RequestBody NoticeUploadReq noticeUploadReq){
        adminNoticeService.uploadNoticeList(mapper.toEntityNoticeContents(noticeUploadReq.getContentsList()), mapper.toEntityNotice(noticeUploadReq));
        return CommonResponse.onSuccess("업로드 성공");
    }
}

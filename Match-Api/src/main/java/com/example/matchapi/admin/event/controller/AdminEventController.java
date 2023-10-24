package com.example.matchapi.admin.event.controller;

import com.example.matchapi.admin.event.dto.EventUploadReq;
import com.example.matchapi.admin.event.service.AdminEventService;
import com.example.matchapi.event.dto.EventRes;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private final AdminEventService adminEventService;

    @PostMapping("")
    public CommonResponse<String> uploadEventList(@RequestBody EventUploadReq eventUploadReq){
        adminEventService.uploadEventList(eventUploadReq);
        return CommonResponse.onSuccess("업로드 성공");
    }

}

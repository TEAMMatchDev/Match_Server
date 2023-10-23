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
    public CommonResponse<List<EventRes.EventList>> uploadEventList(@RequestBody EventUploadReq eventUploadReq){
        return CommonResponse.onSuccess(adminEventService.uploadEventList(eventUploadReq));
    }

    @PostMapping(value = "/img",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<String> imgUpload(@ModelAttribute MultipartFile imgFile){
        String url = adminEventService.uploadEventImg(imgFile);
        return CommonResponse.onSuccess(url);
    }
}

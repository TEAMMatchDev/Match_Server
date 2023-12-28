package com.example.matchapi.admin.event.controller;

import com.example.matchapi.admin.event.dto.EventUploadReq;
import com.example.matchapi.admin.event.service.AdminEventService;
import com.example.matchapi.event.dto.EventRes;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
@Tag(name = "ADMIN-09-Event🎉 관리자 이벤트 관리 API")
public class AdminEventController {
    private final AdminEventService adminEventService;

    @GetMapping("")
    @Operation(summary = "ADMIN-09-01 이벤트 리스트 조회")
    public CommonResponse<PageResponse<List<EventRes.EventList>>> getEventLists(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ){
        return CommonResponse.onSuccess(adminEventService.getEventList(page, size));
    }


    @PostMapping("")
    @Operation(summary = "ADMIN-09-02 이벤트 업로드")
    public CommonResponse<String> uploadEventList(
        @RequestPart("thumbnail") MultipartFile thumbnail,
        @RequestPart("eventUploadReq") EventUploadReq eventUploadReq
    ){
        adminEventService.uploadEventList(thumbnail, eventUploadReq);
        return CommonResponse.onSuccess("업로드 성공");
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "ADMIN-09-03 이벤트 삭제")
    public CommonResponse<String> deleteEvent(@PathVariable Long eventId){
        adminEventService.deleteEvent(eventId);
        return CommonResponse.onSuccess("삭제 성공");
    }

    
}

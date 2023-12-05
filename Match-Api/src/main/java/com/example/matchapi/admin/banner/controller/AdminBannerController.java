package com.example.matchapi.admin.banner.controller;

import com.example.matchapi.admin.banner.service.AdminBannerService;
import com.example.matchapi.banner.dto.BannerReq;
import com.example.matchapi.banner.dto.BannerRes;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.banner.enums.BannerType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/banners")
@Tag(name = "ADMIN-08-Banner💳 관리자 배너 관리 API")
public class AdminBannerController {
    private final AdminBannerService adminBannerService;
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "ADMIN-08-01 배너 업로드")
    public CommonResponse<List<BannerRes.BannerList>> uploadBanner(
            //@RequestPart BannerType bannerType,
            @RequestPart MultipartFile bannerImage,
            @RequestPart BannerReq.BannerUpload bannerUploadDto
            ){
        System.out.println(bannerUploadDto.getContentsUrl());
        return CommonResponse.onSuccess(adminBannerService.uploadBanner(BannerType.EVENT, bannerImage, bannerUploadDto));
    }
}

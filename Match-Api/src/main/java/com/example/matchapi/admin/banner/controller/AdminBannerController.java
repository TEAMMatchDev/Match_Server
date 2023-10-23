package com.example.matchapi.admin.banner.controller;

import com.example.matchapi.admin.banner.service.AdminBannerService;
import com.example.matchapi.banner.dto.BannerReq;
import com.example.matchapi.banner.dto.BannerRes;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.banner.enums.BannerType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/banners")
public class AdminBannerController {
    private final AdminBannerService adminBannerService;
    @PostMapping("")
    public CommonResponse<List<BannerRes.BannerList>> uploadBanner(
            @RequestParam BannerType bannerType,
            @RequestPart MultipartFile bannerImage,
            @RequestPart BannerReq.BannerUpload bannerUploadDto
            ){
        System.out.println(bannerUploadDto.getContentsUrl());
        return CommonResponse.onSuccess(adminBannerService.uploadBanner(bannerType, bannerImage, bannerUploadDto));
    }
}

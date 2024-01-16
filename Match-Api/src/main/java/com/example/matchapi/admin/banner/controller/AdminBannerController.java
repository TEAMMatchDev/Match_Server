package com.example.matchapi.admin.banner.controller;

import com.example.matchapi.admin.banner.service.AdminBannerService;
import com.example.matchapi.banner.dto.BannerReq;
import com.example.matchapi.banner.dto.BannerRes;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
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
            @RequestPart MultipartFile bannerImage,
            @Parameter(name = "bannerUploadDto", required = true) @RequestPart(value = "bannerUploadDto", required = true) BannerReq.BannerUpload bannerUploadDto
            ){
        return CommonResponse.onSuccess(adminBannerService.uploadBanner(BannerType.CONTENTS, bannerImage, bannerUploadDto));
    }

    @GetMapping("")
    @Operation(summary = "ADMIN-08-02 배너 조회")
    public CommonResponse<PageResponse<List<BannerRes.BannerAdminListDto>>> getBannerLists(
        @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
        @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "10") int size

    ){
        return CommonResponse.onSuccess(adminBannerService.getBannerLists(page, size));
    }

    @DeleteMapping("/{bannerId}")
    @Operation(summary = "ADMIN-08-03 배너 삭제")
    public CommonResponse<String> deleteBanner(@PathVariable Long bannerId){
        adminBannerService.deleteBanner(bannerId);
        return CommonResponse.onSuccess("삭제 성공");
    }

    @PatchMapping("/{bannerId}")
    @Operation(summary = "ADMIN-08-04 배너 수정")
    public CommonResponse<String> patchBanner(
        @PathVariable Long bannerId,
        @RequestPart(value = "bannerImage", required = false) MultipartFile bannerImage,
        @RequestPart("bannerPatchDto") BannerReq.BannerPatchDto bannerPatchDto){
        adminBannerService.patchBanner(bannerId, bannerPatchDto, bannerImage);
        return CommonResponse.onSuccess("수정 성공");
    }
}

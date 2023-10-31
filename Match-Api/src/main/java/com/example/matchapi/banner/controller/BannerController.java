package com.example.matchapi.banner.controller;

import com.example.matchapi.banner.dto.BannerRes;
import com.example.matchapi.banner.service.BannerService;
import com.example.matchcommon.reponse.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/banners")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "06-Banner💳", description = "배너 API")
public class BannerController {
    private final BannerService bannerService;

    @GetMapping
    @Operation(
            summary = "06-01 배너 조회",
            description = "BannerType 이 Event 면 eventId 사용후 이벤트 페이지로 이동,\n" +
            "Contents 면 contentsUrl 사용후 링크로 이동")
    public CommonResponse<List<BannerRes.BannerList>> getBannerList(){
        return CommonResponse.onSuccess(bannerService.getBannerList());
    }
}

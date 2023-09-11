package com.example.matchapi.donation.controller;

import com.example.matchapi.donation.dto.DonationTemporaryRes;
import com.example.matchapi.donation.service.DonationTemporaryService;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "07 임시 기부 정보 받기 API")
@RequestMapping("/donation-temporaries")
public class DonationTemporaryController {
    private final DonationTemporaryService donationTemporaryService;

    @Operation(summary = "07-01 유저 로그인 후 유저정보 받아오기")
    @GetMapping("/user-info")
    public CommonResponse<DonationTemporaryRes.UserInfo> getUserInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal User user
            ){
        return CommonResponse.onSuccess(donationTemporaryService.getUserInfo(user));
    }
}

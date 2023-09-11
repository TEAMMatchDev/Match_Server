package com.example.matchapi.donation.controller;

import com.example.matchapi.donation.dto.DonationTemporaryReq;
import com.example.matchapi.donation.dto.DonationTemporaryRes;
import com.example.matchapi.donation.service.DonationTemporaryService;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.donationTemporary.entity.DonationTemporary;
import com.example.matchdomain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @Operation(summary = "07-02 유저 임시 기부정보")
    @PostMapping("")
    public CommonResponse<String> postDonationTemporary(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Valid @RequestBody DonationTemporaryReq.DonationInfo donationInfo
            ){
        donationTemporaryService.postDonationTemporary(user, donationInfo);
        return CommonResponse.onSuccess("기부 요청 성공");
    }
}

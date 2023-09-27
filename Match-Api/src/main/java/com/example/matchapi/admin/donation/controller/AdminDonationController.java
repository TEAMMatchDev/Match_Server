package com.example.matchapi.admin.donation.controller;

import com.example.matchapi.donation.dto.DonationReq;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.service.DonationService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/donations")
@Tag(name = "ADMIN-05-Donation💸 관리자 기부금 관련 API 입니다.", description = "기부금 관련 API 입니다.")
@Slf4j
public class AdminDonationController {
    private final DonationService donationService;
    @GetMapping("")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "ADMIN-05-01💸 기부금 현황파악 API.",description = "기부금 현황파악 API 입니다.")
    public CommonResponse<DonationRes.DonationInfo> getDonationInfo(){
        DonationRes.DonationInfo info = donationService.getDonationInfo();
        return CommonResponse.onSuccess(info);
    }

    @GetMapping("/{donationId}")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "ADMIN-05-02 기부금 상세조회 API", description = "기부금 상세조회 API")
    public CommonResponse<DonationRes.DonationDetail> getDonationDetail(@PathVariable Long donationId){
        return CommonResponse.onSuccess(donationService.getDonationDetail(donationId));
    }

    @PostMapping("/enforce")
    @ApiErrorCodeExample({UserAuthErrorCode.class, RequestErrorCode.class})
    @Operation(summary = "ADMIN-05-03 기부금 집행 내역 POST API", description = "기부금 집행 API")
    public CommonResponse<String> enforceDonation(@RequestBody DonationReq.EnforceDonation enforceDonation){
        return CommonResponse.onSuccess("성공");
    }
}

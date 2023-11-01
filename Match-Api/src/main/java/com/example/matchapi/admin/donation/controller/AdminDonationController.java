package com.example.matchapi.admin.donation.controller;

import com.example.matchapi.admin.donation.service.AdminDonationService;
import com.example.matchapi.donation.dto.DonationReq;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.service.DonationService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/donations")
@Tag(name = "ADMIN-05-Donation💸 관리자 기부금 관련 API 입니다.", description = "기부금 관련 API 입니다.")
@Slf4j
public class AdminDonationController {
    private final AdminDonationService adminDonationService;
    @GetMapping("")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "ADMIN-05-01💸 기부금 현황파악 API.",description = "기부금 현황파악 API 입니다.")
    public CommonResponse<DonationRes.DonationInfo> getDonationInfo(){
        DonationRes.DonationInfo info = adminDonationService.getDonationInfo();
        return CommonResponse.onSuccess(info);
    }

    @GetMapping("/{donationId}")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "ADMIN-05-02 기부금 상세조회 API", description = "기부금 상세조회 API")
    public CommonResponse<DonationRes.DonationDetail> getDonationDetail(@PathVariable Long donationId){
        return CommonResponse.onSuccess(adminDonationService.getDonationDetail(donationId));
    }

    @PostMapping(value = "/enforce", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiErrorCodeExample({UserAuthErrorCode.class, RequestErrorCode.class})
    @Operation(summary = "ADMIN-05-03 기부금 집행 전달완료 POST API", description = "기부금 집행 API")
    public CommonResponse<String> enforceDonation(
            @RequestPart("imageLists") List<MultipartFile> imageLists,
            @Parameter(
                    description = "",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
            @RequestPart("enforceDonation") DonationReq.EnforceDonation enforceDonation){
        adminDonationService.enforceDonation(imageLists, enforceDonation);
        return CommonResponse.onSuccess("성공");
    }
}

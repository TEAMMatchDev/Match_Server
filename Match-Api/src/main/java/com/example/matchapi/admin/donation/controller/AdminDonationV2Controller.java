package com.example.matchapi.admin.donation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.matchapi.admin.donation.service.AdminDonationService;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.user.exception.UserAuthErrorCode;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "ADMIN-05-Donation V2💸 관리자 기부금 관련 API 입니다.", description = "기부금 관련 API 입니다.")
@RequestMapping("/admin/v2/donations")
@RequiredArgsConstructor
public class AdminDonationV2Controller {
	private final AdminDonationService adminDonationService;
	@GetMapping("/regular")
	@ApiErrorCodeExample(UserAuthErrorCode.class)
	@Operation(summary = "ADMIN-05-01-01 V2 정기 결제 현황 파악", description = "정기 결제 현황파악")
	public CommonResponse<DonationRes.RegularInfoV2Dto> getRegularInfoV2(){
		return CommonResponse.onSuccess(adminDonationService.getRegularInfoV2());
	}
}

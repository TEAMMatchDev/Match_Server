package com.example.matchapi.admin.donationTemporary.controller;

import com.example.matchapi.donation.dto.DonationTemporaryReq;
import com.example.matchapi.donation.dto.DonationTemporaryRes;
import com.example.matchapi.donation.service.DonationTemporaryService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.donationTemporary.entity.Deposit;
import com.example.matchdomain.donationTemporary.entity.DonationKind;
import com.example.matchdomain.donationTemporary.exception.AdminDonationRequestErrorCode;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "ADMIN-06-Donation-Temporary💸 관리자 임시 기부금 관련 API 입니다.", description = "임시 기부금 관련 API 입니다.")
@RequestMapping("/admin/donation-temporaries")
public class AdminDonationTemporaryController {
    private final DonationTemporaryService donationTemporaryService;
    @GetMapping("")
    @Operation(summary = "ADMIN-06-01💸 기부금 요청 리스트 조회 API.",description = "기부금 요청 리스트 조회 API 입니다.")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    public CommonResponse<PageResponse<List<DonationTemporaryRes.DonationRequestAdminList>>> getDonationRequestList(
            @Parameter(description = "입금 유무") @RequestParam(required = false,defaultValue = "ALL") Deposit deposit,
            @Parameter(description = "이름") @RequestParam(required = false) String content,
            @Parameter(description = "페이지", example = "0")@RequestParam(required = false, defaultValue = "0")@Min(value = 0) int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "10") int size
    ){
        System.out.println(deposit);
        System.out.println(content);
        return CommonResponse.onSuccess(donationTemporaryService.getDonationRequestList(deposit, page ,size,content));
    }


    @Operation(summary = "ADMIN-06-02💸 기부금 입금내역 POST API.",description = "기부금 입금내역 POST API 입니다.")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @PostMapping("")
    public CommonResponse<String> postDonationDeposit(@RequestBody DonationTemporaryReq.DonationDeposit donationDeposit){
        donationTemporaryService.postDonationDeposit(donationDeposit);
        return CommonResponse.onSuccess("기부금 입금내역 추가 성공");
    }


    @ApiErrorCodeExample({UserAuthErrorCode.class, AdminDonationRequestErrorCode.class})
    @GetMapping("/{donationRequestId}")
    @Operation(summary = "ADMIN-06-02💸 기부금 입금내역 등록 전 사용 API 정보 불러오기.", description = "기부금 입금내역 API 입니다.")
    public CommonResponse<DonationTemporaryRes.DonationDetail> getDonationInfo(@PathVariable("donationRequestId") Long donationRequestId){
        return CommonResponse.onSuccess(donationTemporaryService.getDonationInfo(donationRequestId));
    }



}

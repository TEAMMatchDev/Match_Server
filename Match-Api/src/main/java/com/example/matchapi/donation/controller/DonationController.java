package com.example.matchapi.donation.controller;

import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.service.DonationService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.donation.exception.DonationListErrorCode;
import com.example.matchdomain.donation.exception.DonationRefundErrorCode;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/donations")
@RequiredArgsConstructor
@Tag(name = "05-Donation💸",description = "기부금 관련 API 입니다.")
public class DonationController {
    private final DonationService donationService;
    @GetMapping("")
    @ApiErrorCodeExample({DonationListErrorCode.class, UserAuthErrorCode.class})
    @Operation(summary = "05-01 Donation💸 기부 리스트 조회")
    public CommonResponse<PageResponse<List<DonationRes.DonationList>>> getDonationList(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "10") int size,
            @Parameter(description = "필터 전체 조회 0 집행 전 1 집행 중 2 집행완료 3") @RequestParam(required = false,defaultValue = "0") int filter){
        return CommonResponse.onSuccess(donationService.getDonationList(user.getId(),filter,page, size));
    }

    @PatchMapping("/{donationId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class, DonationRefundErrorCode.class})
    @Operation(summary = "05-02 Donation💸 가부금 환불")
    public CommonResponse<String> refundDonation(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "기부금 id") @PathVariable Long donationId
            ){
        donationService.refundDonation(user,donationId);
        return CommonResponse.onSuccess("기부금 환불 성공");
    }

    @GetMapping("/flame")
    @ApiErrorCodeExample(UserAuthErrorCode.class)
    @Operation(summary = "05-03 Donation💸 홈화면 불꽃이 필터링 조회")
    public CommonResponse<PageResponse<List<DonationRes.FlameList>>> getFlameList(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "페이지", example = "0") @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false, defaultValue = "10") int size,
            @Parameter(description = "불꽃이 필터링 0 = 불꽃이 전체, 1 = 전달 전 불꽃이, 2 = 전달 중인 불꽃이, 3 = 전달 완료된 불꽃이", example = "0") @RequestParam(required = false, defaultValue = "0") int flame,
            @Parameter(description = "정렬 필터링 0 = 최신순, 1 = 오래된 순, 2 = 기부금액 큰 순, 3 = 기부금액 작은 순", example = "0") @RequestParam(required = false, defaultValue = "0") int order,
            @Parameter(description = "검색어")  @RequestParam(required = false) String content
    ){

        return CommonResponse.onSuccess(donationService.getFlameList(user, page, size, flame, order, content));
    }




}

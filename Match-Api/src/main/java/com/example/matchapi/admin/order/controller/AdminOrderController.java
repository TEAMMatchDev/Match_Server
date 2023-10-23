package com.example.matchapi.admin.order.controller;

import com.example.matchapi.order.service.OrderService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.donation.entity.enums.DonationStatus;
import com.example.matchdomain.donation.exception.DonationGerErrorCode;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "ADMIN-04-Order💸 관리자 결제 관련 API 입니다.", description = "결제 관리 API 입니다.")
public class AdminOrderController {
    private final OrderService orderService;
    @Operation(summary = "ADMIN-ORDER-04-01💸 기부금 환불 처리 API", description = "관리자 기부금 환불처리 API 입니다.")
    @ApiErrorCodeExample({UserAuthErrorCode.class, DonationGerErrorCode.class})
    @PatchMapping("/refund/{donationId}")
    public CommonResponse<String> refundDonation(@PathVariable Long donationId){
        orderService.adminRefundDonation(donationId);
        return CommonResponse.onSuccess("환불 성공");
    }

    @Operation(summary = "ADMIN-ORDER-04-02💸 기부금 상태 변경 API", description = "관리자 기부금 상태변경 API 입니다,")
    @ApiErrorCodeExample({UserAuthErrorCode.class,DonationGerErrorCode.class})
    @PatchMapping("/{donationId}")
    public CommonResponse<String> modifyDonationStatus(@RequestParam("donationStatus")DonationStatus donationStatus, @PathVariable Long donationId){
        orderService.modifyDonationStatus(donationId, donationStatus);
        return CommonResponse.onSuccess("기부 상태 수정 완료 : " + donationStatus.getName());
    }


}

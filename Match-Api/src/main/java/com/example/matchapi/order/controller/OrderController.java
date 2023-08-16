package com.example.matchapi.order.controller;

import com.example.matchapi.common.aop.CheckIdExist;
import com.example.matchapi.order.dto.OrderReq;
import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.order.service.OrderService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.exception.errorcode.OtherServerErrorCode;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.donation.exception.DeleteCardErrorCode;
import com.example.matchdomain.order.exception.RegistrationCardErrorCode;
import com.example.matchdomain.project.exception.ProjectErrorCode;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import com.example.matchinfrastructure.pay.nice.dto.NicePaymentAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequestMapping("/order")
@RequiredArgsConstructor
@RestController
@Slf4j
@Tag(name = "04-Order💸",description = "NicePayment 결제 API")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/test/pay")
    @ApiErrorCodeExample(OtherServerErrorCode.class)
    @Operation(summary= "04-00 Order💸 결제 인증용 API 사용 X 테스트용",description = "결제 인증용 API 입니다 테스트 용")
    public CommonResponse<NicePaymentAuth> requestPayment(@RequestParam String tid,
                                                          @RequestParam Long amount){
        log.info("04-00 Order 결제 인증 테스트용 API 결제 ID: " + tid + " 결제 금액 " +amount);
        return CommonResponse.onSuccess(orderService.authPayment(tid, amount));
    }

    @PostMapping("/test/cancel/pay")
    @ApiErrorCodeExample(OtherServerErrorCode.class)
    @Operation(summary= "04-00 Order💸 결제 취소용 API 사용 X 테스트용",description = "결제 인증용 API 입니다 테스트 용")
    public CommonResponse<NicePaymentAuth> cancelPayment(@RequestParam String tid,
                                                          @RequestParam String orderId){
        log.info("04-00 Order 결제 취소 테스트용 API 결제 ID: " + tid + " 주문 번호 " +orderId);
        return CommonResponse.onSuccess(orderService.cancelPayment(tid, orderId));
    }

    @PostMapping("/pay/{projectId}")
    @ApiErrorCodeExample({OtherServerErrorCode.class, UserAuthErrorCode.class, RequestErrorCode.class, ProjectErrorCode.class})
    @Operation(summary= "04-01 Order💸 결제 API 사용",description = "결제 API 입니다")
    @CheckIdExist
    public CommonResponse<String> requestPayment(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "프로젝트 ID", example = "1") @PathVariable("projectId") Long projectId,
            @Valid @RequestBody OrderReq.OrderDetail orderDetail){
        log.info("04-03 Order 결제 인증용 API 결제 ID: " + orderDetail.getTid() + " 결제 금액 " + orderDetail.getAmount() +" 기부 프로젝트 ID : " + projectId);
        return CommonResponse.onSuccess(orderService.requestPayment(user , orderDetail, projectId));
    }


    @PostMapping("/pay/card")
    @ApiErrorCodeExample({UserAuthErrorCode.class, OtherServerErrorCode.class, RegistrationCardErrorCode.class})
    @Operation(summary = "04-02 Order💸 정기 결제용 카드 등록 api",description = "정기 결제를 위한 카드 등록 API 입니다.")
    public CommonResponse<String> registrationCard(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Valid @RequestBody OrderReq.RegistrationCard registrationCard){
        orderService.registrationCard(user, registrationCard);
        return CommonResponse.onSuccess("카드 등록 성공");
    }

    @GetMapping("/pay/card")
    @ApiErrorCodeExample({UserAuthErrorCode.class})
    @Operation(summary = "04-03 Order💸 정기 결제용 카드 조회 api",description = "정기 결제를 위한 카드 조회 API 입니다..")
    public CommonResponse<List<OrderRes.UserBillCard>> getUserBillCard(@Parameter(hidden = true) @AuthenticationPrincipal User user){
        return CommonResponse.onSuccess(orderService.getUserBillCard(user));
    }

    @DeleteMapping("/pay/card/{cardId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class, DeleteCardErrorCode.class})
    @Operation(summary = "04-04 Order💸 정기 결제용 카드 삭제 api",description = "정기 결제를 위한 카드 삭제 API 입니다..")
    @CheckIdExist
    public CommonResponse<String> deleteBillCard(@Parameter(hidden = true) @AuthenticationPrincipal User user,
                                                                      @Parameter(description = "카드 ID", example = "1") @PathVariable("cardId") Long cardId){
        orderService.deleteBillCard(cardId);
        return CommonResponse.onSuccess("삭제 성공");
    }

    @PostMapping("/pay/card/{cardId}")
    @ApiErrorCodeExample({UserAuthErrorCode.class, OtherServerErrorCode.class})
    @Operation(summary = "04-05 Order💸 정기 결제 등록 api",description = "정기 결제 신청하기 API 입니다.")
    @CheckIdExist
    public CommonResponse<String> regularDonation(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Parameter(description = "카드 id",example = "1") @PathVariable Long cardId,
            @Valid @RequestBody OrderReq.RegularDonation regularDonation){
        orderService.regularDonation(user, regularDonation, cardId);
        return CommonResponse.onSuccess("정기 결제 등록 성공");
    }

}

package com.example.matchapi.order.controller;

import com.example.matchapi.order.dto.OrderReq;
import com.example.matchapi.order.service.OrderService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.exception.errorcode.OtherServerErrorCode;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import com.example.matchinfrastructure.pay.nice.dto.NicePaymentAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequestMapping("/order")
@RequiredArgsConstructor
@RestController
@Tag(name = "04-Order💸",description = "NicePayment 결제 API")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/test/pay")
    @ApiErrorCodeExample(OtherServerErrorCode.class)
    @Operation(summary= "04-00 Order💸 결제 인증용 API 사용 X 테스트용",description = "결제 인증용 API 입니다 테스트 용")
    public CommonResponse<NicePaymentAuth> requestPayment(@RequestParam String tid,
                                                          @RequestParam Long amount){
        return CommonResponse.onSuccess(orderService.authPayment(tid, amount));
    }

    @PostMapping("/test/cancel/pay")
    @ApiErrorCodeExample(OtherServerErrorCode.class)
    @Operation(summary= "04-00 Order💸 결제 취소용 API 사용 X 테스트용",description = "결제 인증용 API 입니다 테스트 용")
    public CommonResponse<NicePaymentAuth> cancelPayment(@RequestParam String tid,
                                                          @RequestParam String orderId){
        return CommonResponse.onSuccess(orderService.cancelPayment(tid, orderId));
    }
    @PostMapping("/pay")
    @ApiErrorCodeExample({OtherServerErrorCode.class, UserAuthErrorCode.class, RequestErrorCode.class})
    @Operation(summary= "04-01 Order💸 결제 API 사용",description = "결제 API 입니다")
    public CommonResponse<String> requestPayment(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @Valid @RequestBody OrderReq.OrderDetail orderDetail){
        orderService.requestPayment(user.getId(), orderDetail);
        return CommonResponse.onSuccess("결제 성공");
    }

}

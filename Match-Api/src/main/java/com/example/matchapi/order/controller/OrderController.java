package com.example.matchapi.order.controller;

import com.example.matchapi.order.service.OrderService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.exception.errorcode.OtherServerErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchinfrastructure.pay.nice.dto.NicePaymentAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/order")
@RequiredArgsConstructor
@RestController
@Tag(name = "04-Order💸",description = "NicePayment 결제 API")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/pay")
    @ApiErrorCodeExample(OtherServerErrorCode.class)
    @Operation(summary= "04-00 Order💸 결제 인증용 API 사용 X 테스트용",description = "결제 인증용 API 입니다 테스트 용도")
    public CommonResponse<NicePaymentAuth> requestPayment(@RequestParam String tid,
                                                          @RequestParam Long amount){
        return CommonResponse.onSuccess(orderService.authPayment(tid, amount));
    }

}

package com.example.matchapi.portone.controller;

import com.example.matchapi.portone.dto.PaymentReq;
import com.example.matchapi.portone.service.PaymentService;
import com.example.matchapi.project.dto.ProjectReq;
import com.example.matchcommon.reponse.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@Tag(name = "08-PortOne💸",description = "PortOne 결제 API")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping("/validate")
    @Operation(summary = "08-01 Payment 가격 검증💸")
    public CommonResponse<String> validatePayment(@RequestBody PaymentReq.ValidatePayment validatePayment){
        paymentService.checkPayment(validatePayment);
        return CommonResponse.onSuccess("결제 성공");
    }

    @PostMapping("/refund")
    @Deprecated
    public CommonResponse<String> refundPayment(@RequestParam String impUid){
        paymentService.refundPayment(impUid);
        return CommonResponse.onSuccess("환불 성공");
    }
}

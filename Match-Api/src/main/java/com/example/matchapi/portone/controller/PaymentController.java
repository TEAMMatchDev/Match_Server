package com.example.matchapi.portone.controller;

import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.order.service.OrderRequestService;
import com.example.matchapi.portone.dto.PaymentReq;
import com.example.matchapi.portone.mapper.PaymentMapper;
import com.example.matchapi.portone.service.PaymentService;
import com.example.matchapi.project.service.ProjectService;
import com.example.matchapi.user.service.UserService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.order.exception.PortOneAuthErrorCode;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.redis.entity.OrderRequest;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import com.example.matchinfrastructure.pay.portone.dto.PortOneWebhook;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@Tag(name = "08-PortOne💸",description = "PortOne 결제 API")
@Slf4j
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentMapper mapper = PaymentMapper.INSTANCE;
    private final UserService userService;
    private final ProjectService projectService;
    private final OrderRequestService orderRequestService;
    @Value("${spring.config.activate.on-profile}")
    private String profile;

    @PostMapping("/validate")
    @Operation(summary = "08-01 Payment 가격 검증💸", description = "결제 검증용 API 해당 API")
    @ApiErrorCodeExample({UserAuthErrorCode.class, PortOneAuthErrorCode.class})
    public CommonResponse<OrderRes.CompleteDonation> validatePayment(@RequestBody PaymentReq.ValidatePayment validatePayment){
        log.info("가격 검증");
        OrderRequest orderRequest = orderRequestService.findByOrderIdForPayment(validatePayment.getOrderId());

        User user = userService.findByUser(orderRequest.getUserId());

        Project project = projectService.findByProject(orderRequest.getProjectId());

        return CommonResponse.onSuccess(paymentService.checkPayment(mapper.toPaymentValidationCommand(orderRequest, user, project, validatePayment)));
    }

    /*
    @PostMapping("/web-hook")
    public CommonResponse<String> webhookAlert(@RequestBody PortOneWebhook portOneWebhook){
        if(profile.equals("prod")) {
            OrderRequest orderRequest = orderRequestService.findByOrderIdForPayment(portOneWebhook.getMerchant_uid());
            paymentService.webHookCheck(portOneWebhook, orderRequest);
        }
        return CommonResponse.onSuccess("인증 성공");
    }

     */
}

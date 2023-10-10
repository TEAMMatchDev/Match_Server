package com.example.matchapi.common;

import com.example.matchapi.order.helper.DataEncrypt;
import com.example.matchapi.order.service.OrderService;
import com.example.matchcommon.properties.NicePayProperties;
import com.example.matchdomain.redis.entity.OrderRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NicepayController {
    private final OrderService orderService;
    private final NicePayProperties nicePayProperties;
    @Value("${web.return.url}")
    private String redirectUrl;
    @Value("${server.host}")
    private String serverHost;
    private final DataEncrypt dataEncrypt;

    /*
    @RequestMapping("")
    public String indexDemo(
            @RequestParam String method,
            @RequestParam String orderId,
            @RequestParam String productName,
            @RequestParam int amount,
            Model model) {
        model.addAttribute("method",method);
        model.addAttribute("orderId", orderId);
        model.addAttribute("productName", productName);
        model.addAttribute("amount", amount);
        model.addAttribute("clientId", nicePayProperties.getClient());
        model.addAttribute("returnUrl",serverHost+"/serverAuth");
        return "index";
    }

    @RequestMapping("/goPay")
    public String goPay(
            @RequestParam(required = false) String method,
            @RequestParam String orderId,
            @RequestParam int amount,
            Model model
    ){
        String ediDate 			= orderService.getyyyyMMddHHmmss();
        String hashString 		= dataEncrypt.encrypt(ediDate + orderId + amount + nicePayProperties.getKey());
        model.addAttribute("payMethod", "CreditCard");
        model.addAttribute("goodsName", "Sample Product");
        model.addAttribute("Amt", amount);
        model.addAttribute("mid", nicePayProperties.getMid());
        model.addAttribute("moid", orderId);
        model.addAttribute("EdiDate", ediDate);
        model.addAttribute("signData", hashString);
       // model.addAttribute("ReturnURL", "http://localhost:8080/nicepay3.0_utf-8/payResult_utf.jsp");
        return "gopay";
    }

    @RequestMapping("/serverAuth")
    public RedirectView requestPaymentAuth(
            @RequestParam String tid,
            @RequestParam Long amount) throws IOException {
        log.info("04-01 Order 결제 인증용 API 결제 ID: " + tid + " 결제 금액 " + amount);
        log.info("URL : " + redirectUrl);
        orderService.requestPaymentAuth(tid, amount);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(redirectUrl+"/auth/payComplete/once");
        return redirectView;
    }

     */
}
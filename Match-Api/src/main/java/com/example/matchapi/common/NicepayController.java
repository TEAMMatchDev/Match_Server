package com.example.matchapi.common;

import com.example.matchapi.order.service.OrderService;
import com.example.matchcommon.properties.NicePayProperties;
import com.example.matchdomain.redis.entity.OrderRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

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

    @RequestMapping("")
    public String indexDemo(Model model) {

        String orderId = orderService.saveRequest(2L);
        model.addAttribute("method","kakaopay");
        model.addAttribute("orderId", orderId);
        model.addAttribute("clientId", nicePayProperties.getClient());
        return "/index";
    }

    @RequestMapping("/serverAuth")
    public String requestPaymentAuth(
            @RequestParam String tid,
            @RequestParam Long amount,
            Model model){
        log.info("04-03 Order 결제 인증용 API 결제 ID: " + tid + " 결제 금액 " + amount);
        orderService.requestPaymentAuth(tid, amount);
        model.addAttribute("resultMsg","성공");
        return "/response";
    }

}
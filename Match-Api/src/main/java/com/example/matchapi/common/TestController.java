package com.example.matchapi.common;

import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchinfrastructure.aligo.service.AligoInfraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final AligoInfraService aligoInfraService;

    @GetMapping("")
    public CommonResponse<String> testAlimTalk(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone) {
        aligoInfraService.sendAlimTalkForPayment(phone, name);
        return CommonResponse.onSuccess("성공");
    }
}
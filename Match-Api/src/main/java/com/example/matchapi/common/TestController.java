package com.example.matchapi.common;

import com.example.matchapi.common.security.JwtService;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchinfrastructure.aligo.service.AligoInfraService;
import com.example.matchinfrastructure.match_aligo.dto.AlimTalkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import static com.example.matchinfrastructure.aligo.dto.AlimType.EXECUTION;
import static com.example.matchinfrastructure.aligo.dto.AlimType.PAYMENT;

@RestController
@RequiredArgsConstructor
@Profile("localDev")
@RequestMapping("/test")
public class TestController {
    private final AligoInfraService aligoInfraService;

    @GetMapping("")
    public CommonResponse<String> testAlimTalk(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone) {
        aligoInfraService.sendAlimTalkTest(phone, name, PAYMENT);

        //aligoInfraService.sendAlimTalk(new AlimTalkDto(1L,name, phone, "TBT", "강아지 사료"), EXECUTION);
        return CommonResponse.onSuccess("성공");
    }
}
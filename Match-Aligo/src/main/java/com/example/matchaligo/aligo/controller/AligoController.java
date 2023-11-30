package com.example.matchaligo.aligo.controller;

import com.example.matchaligo.aligo.dto.AligoReq;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchinfrastructure.aligo.dto.AlimTalkReq;
import com.example.matchinfrastructure.aligo.dto.AlimType;
import com.example.matchinfrastructure.aligo.service.AligoInfraService;
import com.example.matchinfrastructure.match_aligo.dto.AlimTalkDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "ALIGO 문자 관련 API")
@RequestMapping("/send")
public class AligoController {
    private final AligoInfraService aligoInfraService;
    @ApiErrorCodeExample(RequestErrorCode.class)

    @Operation(summary= "01-01🔑 회원 문자인증 요청", description = "회원 문자인증 용 API 입니다.")
    @GetMapping(value="")
    public CommonResponse<String> checkPhone(@RequestParam String phone, @RequestParam String code){
        log.info("01-01 비회원 문자인증 = " + phone);
        aligoInfraService.sendPhone(phone, code);
        return CommonResponse.onSuccess("문자 전송 성공");
    }

    @PostMapping("/alim-talk")
    public CommonResponse<String> sendAlimTalk(@RequestParam AlimType alimType, @RequestBody AlimTalkDto alimTalk){
        aligoInfraService.sendAlimTalk(alimTalk, alimType);
        return CommonResponse.onSuccess("알림톡 전송 성공");
    }

}

package com.example.matchaligo.aligo.controller;

import com.example.matchaligo.aligo.service.AligoService;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "ALIGO 문자 관련 API")
@RequestMapping("/send")
public class AligoController {
    private final AligoService aligoService;
    @ApiErrorCodeExample(RequestErrorCode.class)

    @Operation(summary= "01-01🔑 회원 문자인증 요청", description = "회원 문자인증 용 API 입니다.")
    @GetMapping(value="")
    public CommonResponse<String> checkPhone(@RequestParam String phone, @RequestParam String code){
        log.info("01-01 비회원 문자인증 = " + phone);
        aligoService.sendPhone(phone, code);
        return CommonResponse.onSuccess("문자 전송 성공");
    }

}

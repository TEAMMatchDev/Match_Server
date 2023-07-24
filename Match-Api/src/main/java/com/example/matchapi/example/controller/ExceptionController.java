package com.example.matchapi.example.controller;

import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.exception.error.CommonResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/examples")
@RequiredArgsConstructor
@SecurityRequirement(name = "access-token")
@Tag(name = "xx. 에러코드 문서화")
public class ExceptionController {

    @GetMapping("/global")
    @Operation(summary = "글로벌 ( 인증 ,서버, 내부 오류등)  관련 에러 코드 나열")
    @ApiErrorCodeExample(CommonResponseStatus.class)
    public void getGlobalErrorCode() {}
}

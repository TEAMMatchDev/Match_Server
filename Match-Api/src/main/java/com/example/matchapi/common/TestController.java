package com.example.matchapi.common;

import com.example.matchapi.common.aop.CheckIdExist;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchcommon.annotation.ApiErrorCodeExample;
import com.example.matchcommon.exception.errorcode.OtherServerErrorCode;
import com.example.matchcommon.exception.errorcode.RequestErrorCode;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequestMapping("/test")
@RestController
@RequiredArgsConstructor
public class TestController {
    private final OrderHelper orderHelper;

    @GetMapping("")
    public void exRedirect3(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("https://naver.com");
    }

    @CheckIdExist
    @GetMapping("/{projectId}/{userId}/{donationId}")
    public CommonResponse<String> getTest(@PathVariable Long projectId, @PathVariable Long userId, @PathVariable Long donationId){
        return CommonResponse.onSuccess("성공");
    }
}

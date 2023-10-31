package com.example.matchapi.common;

import com.example.matchapi.common.aop.CheckIdExist;
import com.example.matchapi.notification.service.NotificationService;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.service.MailService;
import com.example.matchdomain.user.entity.User;
import com.example.matchinfrastructure.fcm.dto.FCMNotificationRequestDto;
import com.example.matchinfrastructure.fcm.service.FcmNotificationService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequestMapping("/test")
@RestController
@RequiredArgsConstructor
public class TestController {
    private final OrderHelper orderHelper;
    private final MailService mailService;
    private final NotificationService notificationService;
    private final FcmNotificationService fcmNotificationService;

    @GetMapping("")
    public void exRedirect3(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("https://naver.com");
    }

    @CheckIdExist
    @GetMapping("/{projectId}/{userId}/{donationId}")
    public CommonResponse<String> getTest(@PathVariable Long projectId, @PathVariable Long userId, @PathVariable Long donationId){
        return CommonResponse.onSuccess("성공");
    }

    @PostMapping("/fcm")
    public String fcmTest(
            @RequestBody FCMNotificationRequestDto fcmNotificationRequestDto
            ){
        fcmNotificationService.testNotification(fcmNotificationRequestDto);
        return "성공";
    }

    @PostMapping("/fcm/user")
    public String fcmUserTest(
            @AuthenticationPrincipal User user,
            @RequestBody FCMNotificationRequestDto fcmNotificationRequestDto
    ){
        fcmNotificationService.testNotification(fcmNotificationRequestDto);
        notificationService.saveTestNotification(user, fcmNotificationRequestDto);

        return "성공";
    }
    /*

    @GetMapping("/email")
    public CommonResponse<String> testEmail(@Parameter String email) throws Exception {
        mailService.sendEmailMessage(email, code);
        return CommonResponse.onSuccess("이메일 전송 성공");
    }

     */
}

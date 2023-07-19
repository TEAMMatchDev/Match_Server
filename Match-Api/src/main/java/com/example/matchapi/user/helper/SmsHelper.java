package com.example.matchapi.user.helper;

import com.example.matchcommon.annotation.Helper;
import com.example.matchcommon.properties.CoolSmsProperties;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

import java.util.Random;

@Helper
public class SmsHelper {

    private final DefaultMessageService messageService;
    private final CoolSmsProperties coolSmsProperties;

    public SmsHelper(CoolSmsProperties coolSmsProperties) {
        this.coolSmsProperties = coolSmsProperties;
        // 반드시 계정 내 등록된 유효한 API 키, API Secret Key를 입력해주셔야 합니다!
        this.messageService = NurigoApp.INSTANCE.initialize(coolSmsProperties.getApi(), coolSmsProperties.getSecret(), "https://api.coolsms.co.kr");
    }

    public String sendSms(String phone){
        String number = createRandomNumber();
        sendOneSms(phone,number);
        return number;
    }

    public String createRandomNumber() {
        Random rand  = new Random();
        String numStr = "";
        for(int i=0; i<6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr+=ran;
        }

        return numStr;
    }

    public void sendOneSms(String phone, String number) {
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom(coolSmsProperties.getSender());
        message.setTo(phone);
        message.setText("[MATCH] 회원님의 인증번호는 [" + number + "] 입니다.");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

    }
}

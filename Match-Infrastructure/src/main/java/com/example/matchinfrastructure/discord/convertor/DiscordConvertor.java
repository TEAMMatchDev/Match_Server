package com.example.matchinfrastructure.discord.convertor;

import com.example.matchcommon.annotation.Convertor;
import com.example.matchinfrastructure.discord.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Convertor
@RequiredArgsConstructor
public class DiscordConvertor {
    private final Environment environment;


    public Message convertToUnknownMessage(String username, Exception exception, HttpServletRequest request) {
        List<Message.Embeds> embedsList = new ArrayList<>();

        embedsList.add(Message.Embeds.builder().title("실행중인 환경").description(Arrays.toString(environment.getActiveProfiles())).build());
        embedsList.add(Message.Embeds.builder().title("에러 내용").description(exception.getMessage()).build());

        return Message
                .builder()
                .content("==================================================\n"+
                        "🚨 Match Server 요청 URI : "+ request.getRequestURI() + " Method : " + request.getMethod() + "에 정의되지 않은 에러가 나타났어요 ! 🚨\n\n" +
                        "호스팅 서버 : " + environment.getProperty("server.host") + "\n\n" +
                        "로그인한 유저 ID : " + username)
                .tts(false)
                .embeds(embedsList)
                .build();
    }

    public Message convertToAlertBatchMessage(String title, int size) {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Message.Embeds> embedsList = new ArrayList<>();
        embedsList.add(Message.Embeds.builder().title("총 결제 예정 수").description(String.valueOf(size)).build());



        return Message
                .builder()
                .content("=====================================\n"+
                        "🔥"+ localDateTime.getDayOfMonth() +"일  "+ title +  "되었어요 ! 🔥\n\n" +
                        "호스팅 서버 : " + environment.getProperty("server.host") + "\n\n" +
                        "시작시간: " + localDateTime.getYear() + "년 "
                        + localDateTime.getMonthValue() + "월 "
                        + localDateTime.getDayOfMonth() + "일 "
                        + localDateTime.getHour() + "시 "
                        + localDateTime.getMinute() + "분 "
                        + localDateTime.getSecond() + "초 \n")
                .tts(false)
                .embeds(embedsList)
                .build();
    }

    public Message convertToAlertFinishMessage(String title, int amount, int size, int successCnt, int trueCnt) {
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Message.Embeds> embedsList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#,###"); // 포맷을 설정합니다.
        String formattedNumber = decimalFormat.format(amount);

        int failCnt = trueCnt - successCnt;

        embedsList.add(Message.Embeds.builder().title("총 결제 금액").description(formattedNumber).build());



        return Message
                .builder()
                .content("=====================================\n"+
                        "🔥"+ localDateTime.getDayOfMonth() +"일  "+ title +  "되었어요 ! 🔥\n\n" +
                        "호스팅 서버 : " + environment.getProperty("server.host") + "\n\n" +
                        "종료시간: " + localDateTime.getYear() + "년 "
                        + localDateTime.getMonthValue() + "월 "
                        + localDateTime.getDayOfMonth() + "일 "
                        + localDateTime.getHour() + "시 "
                        + localDateTime.getMinute() + "분 "
                        + localDateTime.getSecond() + "초 \n\n"
                                + "총 진행 결제 수 : " + trueCnt
                                + "\n성공 횟수 : " + successCnt
                                +"\n실패 횟수 : " +  failCnt)
                .tts(false)
                .embeds(embedsList)
                .build();
    }

    public Message convertToErrorBatchServer(String title, String message) {
        List<Message.Embeds> embedsList = new ArrayList<>();

        embedsList.add(Message.Embeds.builder().title("실행중인 환경").description(Arrays.toString(environment.getActiveProfiles())).build());
        embedsList.add(Message.Embeds.builder().title("에러 내용").description(message).build());

        return Message
                .builder()
                .content("==================================================\n"+
                        "🚨 Match Batch Server 실행중인 스케줄러 : "+ title + "가 실행중에 에러가 나타났어요 ! 🚨\n\n" +
                        "호스팅 서버 : " + environment.getProperty("server.host") + "\n\n" )
                .tts(false)
                .embeds(embedsList)
                .build();
    }

    public Message convertToKnownMessage(String message) {
        List<Message.Embeds> embedsList = new ArrayList<>();

        embedsList.add(Message.Embeds.builder().title("실행중인 환경").description(Arrays.toString(environment.getActiveProfiles())).build());

        return Message
                .builder()
                .content(message)
                .tts(false)
                .embeds(embedsList)
                .build();
    }
}

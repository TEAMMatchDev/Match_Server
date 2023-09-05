package com.example.matchinfrastructure.discord.convertor;

import com.example.matchcommon.annotation.Convertor;
import com.example.matchinfrastructure.discord.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Convertor
@RequiredArgsConstructor
public class DiscordConvertor {
    private final Environment environment;


    public Message Message(User user, Exception exception, HttpServletRequest request) {
        List<Message.Embeds> embedsList = new ArrayList<>();
        String username = "로그인 되지 않은 유저";
        if(user!=null){
            username = user.getUsername();
        }

        embedsList.add(Message.Embeds.builder().title("실행중인 환경").description(Arrays.toString(environment.getActiveProfiles())).build());
        embedsList.add(Message.Embeds.builder().title("에러 내용").description(exception.getMessage()).build());

        return Message
                .builder()
                .content("🚨 Match Server 요청 URI : "+ request.getRequestURI() + " Method : " + request.getMethod() + "에 정의되지 않은 에러가 나타났어요 ! 🚨\n\n" +
                        "호스팅 서버 : " + environment.getProperty("server.host") + "\n\n" +
                        "로그인한 유저 ID : " + username)
                .tts(false)
                .embeds(embedsList)
                .build();
    }
}

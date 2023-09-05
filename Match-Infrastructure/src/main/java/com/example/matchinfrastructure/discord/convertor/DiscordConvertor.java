package com.example.matchinfrastructure.discord.convertor;

import com.example.matchcommon.annotation.Convertor;
import com.example.matchinfrastructure.discord.dto.Message;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Convertor
public class DiscordConvertor {
    public Message Message(User user, Exception exception, HttpServletRequest request) {
        List<Message.Embeds> embedsList = new ArrayList<>();
        embedsList.add(Message.Embeds.builder().title("에러 내용").description(exception.getMessage()).build());
        String username = "로그인 되지 않은 유저";
        if(user!=null){
            username = user.getUsername();
        }

        return Message
                .builder()
                .content("🚨 Match Server 요청 URI : "+ request.getRequestURI() + " Method : " + request.getMethod() + "에 정의되지 않은 에러가 나타났어요 ! 🚨\n\n" +
                        " 로그인한 유저 : " + username + "\n\n")
                .tts(false)
                .embeds(embedsList)
                .build();
    }
}

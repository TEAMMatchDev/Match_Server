package com.example.matchinfrastructure.aligo.dto;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AlimTalkReq {
    private String apikey;
    private String userid;
    private String senderkey;
    private String tpl_code;
    private String sender;
    private String recvname_1;
    private String receiver_1;
    private String emtitle_1;
    private String subject_1;
    private String message_1;
    private String token;
    private String button_1;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Button{
        private String name;

        private String linkType;

        private String linkTypeName;

        private String linkIos;

        private String linkAnd;
    }
}

package com.example.matchdomain.user.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {
    KAKAO("kakao","카카오"),
    NAVER("naver","네이버"),
    APPLE("apple","애플"),
    NORMAL("normal","이메일");

    private final String value;
    private final String name;
    public String getValue() {
        return value;
    }

}

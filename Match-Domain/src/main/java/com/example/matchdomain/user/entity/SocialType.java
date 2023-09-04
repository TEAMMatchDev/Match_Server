package com.example.matchdomain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {
    KAKAO("kakao","카카오"),
    NAVER("naver","네이버"),
    APPLE("apple","애플"),
    NORMAL("normal","전화번호");

    private final String value;
    private final String name;
    public String getValue() {
        return value;
    }

}

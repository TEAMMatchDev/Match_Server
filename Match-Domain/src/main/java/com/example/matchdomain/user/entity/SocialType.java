package com.example.matchdomain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {
    KAKAO("kakao"),
    NAVER("naver"),
    NORMAL("normal");

    private final String value;
    public String getValue() {
        return value;
    }

}

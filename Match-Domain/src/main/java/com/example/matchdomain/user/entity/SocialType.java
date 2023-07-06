package com.example.matchdomain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialType {
    KAKAO("kakao"),
    NAVER("naver"),
    normal("normal");

    private final String value;

    }

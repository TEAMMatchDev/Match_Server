package com.example.matchdomain.donationTemporary.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmMethod {
    KAKAO("KAKAO","카카오"),
    SMS("SMS","문자"),
    EMAIL("EMAIL","이메일");

    private final String value;

    private final String name;
}

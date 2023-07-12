package com.example.matchdomain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    FEMALE("여자"),
    MALE("남자"),
    UNKNOWN("알 수 없음");

    private final String value;

    }

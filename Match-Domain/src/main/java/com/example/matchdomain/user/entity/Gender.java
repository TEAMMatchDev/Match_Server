package com.example.matchdomain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    FEMALE("여성"),
    MALE("남성"),
    UNKNOWN("선택 안함");

    private final String value;

    }

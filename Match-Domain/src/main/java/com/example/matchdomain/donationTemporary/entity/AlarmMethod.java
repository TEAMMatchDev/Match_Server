package com.example.matchdomain.donationTemporary.entity;

import com.example.matchdomain.donation.entity.RegularStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AlarmMethod {
    KAKAO("KAKAO","카카오"),
    SMS("SMS","문자"),
    EMAIL("EMAIL","이메일");

    private final String value;

    private final String name;

    @JsonCreator(mode=JsonCreator.Mode.DELEGATING)
    public static AlarmMethod get(String value) {
        return Arrays.stream(values())
                .filter(type -> type.getValue().equals(value))
                .findAny()
                .orElse(null);
    }
}

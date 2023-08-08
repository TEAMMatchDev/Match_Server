package com.example.matchdomain.donation.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RegularStatus {
    REGULAR("REGULAR","정기 후원"),
    ONE_TIME("ONE_TIME","일회성 후원");

    private final String value;
    private final String name;

    //enum Type 의 filed에 받아오고자 할 때 error 가 발생 하지 않게 null 을 반환해줌
    @JsonCreator(mode=JsonCreator.Mode.DELEGATING)
    public static RegularStatus get(String value) {
        return Arrays.stream(values())
                .filter(type -> type.getValue().equals(value))
                .findAny()
                .orElse(null);
    }
}

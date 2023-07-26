package com.example.matchdomain.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Array;
import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Gender {
    FEMALE("여성"),
    MALE("남성"),
    UNKNOWN("선택 안함");

    private final String value;


    //enum Type 의 filed에 받아오고자 할 때 error 가 발생 하지 않게 null 을 반환해줌
    @JsonCreator(mode=JsonCreator.Mode.DELEGATING)
    public static Gender get(String value) {
        return Arrays.stream(values())
                .filter(type -> type.getValue().equals(value))
                .findAny()
                .orElse(null);
    }
}

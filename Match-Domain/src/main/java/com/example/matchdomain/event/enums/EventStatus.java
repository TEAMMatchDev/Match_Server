package com.example.matchdomain.event.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventStatus {
    UNDER("진행중"),
    FINISH("종료");

    private final String value;
}

package com.example.matchdomain.event.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {
    MATCHING("MATCHING", "매칭 기부"),
    MATCH_EVENT("MATCH_EVENT", "매치 이벤트");
    private final String value;

    private final String type;
}

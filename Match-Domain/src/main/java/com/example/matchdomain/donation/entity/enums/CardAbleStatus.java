package com.example.matchdomain.donation.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardAbleStatus {
    ABLE("ABLE","사용 가능"),
    DISABLE("DISABLE","사용 불가");

    private final String value;
    private final String name;
}

package com.example.matchdomain.donation.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    COMPLETE("COMPLETE"),
    FAIL("FAIL"),
    CANCEL("CANCEL");
    private final String value;
}

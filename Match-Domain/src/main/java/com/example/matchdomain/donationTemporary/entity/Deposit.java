package com.example.matchdomain.donationTemporary.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Deposit {
    EXISTENCE("EXISTENCE","입금 완료"),
    NONEXISTENCE("NONEXISTENCE", "입금 미완료"),
    ALL("ALL", "전체");

    private final String value;
    private final String name;
}

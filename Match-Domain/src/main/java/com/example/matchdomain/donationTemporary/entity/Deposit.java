package com.example.matchdomain.donationTemporary.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Deposit {
    EXISTENCE("EXISTENCE"),
    NONEXISTENCE("NONEXISTENCE");

    private final String value;
}

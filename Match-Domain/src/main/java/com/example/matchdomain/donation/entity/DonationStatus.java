package com.example.matchdomain.donation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DonationStatus {
    EXECUTION_BEFORE("EXECUTION_BEFORE"),
    EXECUTION_UNDER("EXECUTION_UNDER"),
    EXECUTION_SUCCESS("EXECUTION_SUCCESS");

    private final String value;
}

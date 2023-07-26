package com.example.matchdomain.donation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DonationStatus {
    EXECUTION_BEFORE("EXECUTION_BEFORE","집행 전"),
    EXECUTION_UNDER("EXECUTION_UNDER","집행 중"),
    EXECUTION_SUCCESS("EXECUTION_SUCCESS","집행 완료");

    private final String value;
    private final String name;


}

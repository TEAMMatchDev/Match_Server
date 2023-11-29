package com.example.matchdomain.donation.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DonationStatus {
    EXECUTION_BEFORE("EXECUTION_BEFORE","집행 전"),
    EXECUTION_UNDER("EXECUTION_UNDER","집행 중"),
    SOME_EXECUTION("SOME_EXECUTION","일부 집행"),
    EXECUTION_SUCCESS("EXECUTION_SUCCESS","집행 완료"),
    EXECUTION_REFUND("EXECUTION_REFUND","기부금 환불");

    private final String value;
    private final String name;


}

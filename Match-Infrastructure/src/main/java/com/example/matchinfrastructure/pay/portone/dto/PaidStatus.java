package com.example.matchinfrastructure.pay.portone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum PaidStatus {
    paid("성공"),
    ready("가상 계좌 발급"),
    failed("실패"),
    cancelled("결제 취소");
    private final String type;
}

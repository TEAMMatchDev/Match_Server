package com.example.matchdomain.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {
    PAY_SUCCESS("결제 완료 후"),
    PAY_ONE_DAY("결제 완료 하루 이후"),
    UNDER("집행 중일 때"),
    COMPLETE("전달 완료일 때");

    private final String description;
}

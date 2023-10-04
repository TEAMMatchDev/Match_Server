package com.example.matchdomain.donation.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HistoryStatus {
    START("START", "프로젝트 시작"),
    TURN_ON("TURN_ON","후원 시작"),
    CREATE("CREATE","불꽃이 생성"),
    COMPLETE("COMPLETE","전달완료"),
    CHANGE("CHANGE","후원품 변환"),
    FINISH("FINISH","프로젝트 종료");
    private final String value;
    private final String name;
}

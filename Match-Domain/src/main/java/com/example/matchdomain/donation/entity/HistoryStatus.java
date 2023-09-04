package com.example.matchdomain.donation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HistoryStatus {
    CREATE("CREATE","불꽃이 생성"),
    COMPLETE("COMPLETE","전달완료");
    private final String value;
    private final String name;
}

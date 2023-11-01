package com.example.matchdomain.donation.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegularPayStatus {
    PROCEEDING("PROCEEDING","진행중"),
    PROJECT_FINISH("PROJECT_FINISH","프로젝트 종료로 인한 정기후원 종료"),
    USER_CANCEL("USER_CANCEL","유저 후원 취소");
    private final String value;
    private final String name;
}

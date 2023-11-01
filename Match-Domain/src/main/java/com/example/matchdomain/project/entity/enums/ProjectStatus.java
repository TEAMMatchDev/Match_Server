package com.example.matchdomain.project.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectStatus {
    //진행중
    BEFORE_START("BEFORE_START","기부 시작 전"),
    //시작 전
    PROCEEDING("PROCEEDING","기부 진행 중"),
    //마감
    DEADLINE("DEADLINE","기부 마감");

    private final String value;
    private final String name;
}

package com.example.matchdomain.notice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NoticeType {
    EVENT("EVENT", "이벤트 및 행사 공지"),
    SCHEDULE("SCHEDULE", "일정 및 휴무 공지"),
    SERVICE_UPDATE("SERVICE_UPDATE", "서비스 업데이트"),
    JOB("JOB", "채용 및 구인 정보"),
    SECURITY("SECURITY", "보안 공지"),
    IMPORTANT("IMPORTANT", "중요 공지"),
    EMERGENCY("EMERGENCY", "주요 이슈 및 재난 공지"),
    ADMIN("ADMIN", "행정 및 관리 공지"),
    EDUCATION("EDUCATION", "학교 및 교육 공지"),
    HEALTH("HEALTH", "건강 및 의료 공지"),
    PROJECT("PROJECT", "주요 프로젝트 및 연구 공지"),
    SOCIAL("SOCIAL", "사회적 활동 공지");

    private final String value;
    private final String type;
}
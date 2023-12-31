package com.example.matchdomain.notification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    DONATION("DONATION", "기부"),
    NOTICE("NOTICE", "공지"),
    REGULAR_PAYMENT("REGULAR_PAYMENT", "정기결제 신청"),
    TEST("TEST","테스트 알림");

    private final String value;

    private final String type;
}

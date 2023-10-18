package com.example.matchdomain.notification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    DONATION("DONATION", "기부"),
    NOTICE("NOTICE", "공지");

    private final String value;

    private final String type;
}

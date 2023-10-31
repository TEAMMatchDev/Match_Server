package com.example.matchdomain.banner.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BannerType {
    EVENT("EVENT", "이벤트"),
    CONTENTS("CONTENTS", "SNS 콘텐츠");
    private final String value;

    private final String type;
}

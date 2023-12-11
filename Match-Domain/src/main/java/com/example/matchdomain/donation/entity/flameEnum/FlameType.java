package com.example.matchdomain.donation.entity.flameEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlameType {
    TUTORIAL_FLAME("TUTORIAL_FLAME", "튜토리얼 불꽃"),
    NORMAL_FLAME("NORMAL_FLAME", "일반 불꽃");

    private final String value;

    private final String type;
}

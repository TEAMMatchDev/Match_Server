package com.example.matchdomain.donation.entity.flameEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlameType {
    NORMAL_FLAME("NORMAL_FLAME", "일반 불꽃");

    private final String value;

    private final String type;
}

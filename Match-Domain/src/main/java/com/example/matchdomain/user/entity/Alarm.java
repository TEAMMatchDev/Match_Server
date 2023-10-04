package com.example.matchdomain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Alarm {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private final String value;
}

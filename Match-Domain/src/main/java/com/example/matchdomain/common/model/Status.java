package com.example.matchdomain.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    ACTIVE("ACTIVE", "활성화"),
    INACTIVE("INACTIVE", "비활성화");

    private final String value;
    private final String name;
}

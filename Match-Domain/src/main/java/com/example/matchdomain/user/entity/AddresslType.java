package com.example.matchdomain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AddresslType {
    OLD("old"),
    NEW("new");

    private final String value;
    public String getValue() {
        return value;
    }

}

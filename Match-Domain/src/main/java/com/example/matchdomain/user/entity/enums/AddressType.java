package com.example.matchdomain.user.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AddressType {
    OLD("old"),
    NEW("new");

    private final String value;
    public String getValue() {
        return value;
    }

}

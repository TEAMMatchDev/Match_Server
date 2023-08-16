package com.example.matchdomain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum AuthorityEnum {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");
    private final String value;
}

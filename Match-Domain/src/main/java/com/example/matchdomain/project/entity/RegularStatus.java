package com.example.matchdomain.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum RegularStatus {
    REGULAR("REGULAR"),
    ONE_TIME("ONE_TIME");

    private final String value;
}

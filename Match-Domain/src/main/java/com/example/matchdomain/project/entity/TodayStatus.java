package com.example.matchdomain.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TodayStatus {
    TODAY("TODAY"),
    NOT_TODAY("NOT_TODAY");

    private final String value;
}

package com.example.matchdomain.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum RegularStatus {
    REGULAR("REGULAR","정기 후원"),
    ONE_TIME("ONE_TIME","일회성 후원");

    private final String value;
    private final String name;
}

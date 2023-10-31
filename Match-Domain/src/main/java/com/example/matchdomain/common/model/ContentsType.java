package com.example.matchdomain.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContentsType {
    IMG("IMG", "이미지"),
    TEXT("TEXT", "텍스트");

    private final String value;

    private final String type;

}

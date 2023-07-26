package com.example.matchdomain.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageRepresentStatus {
    //대표 사진
    REPRESENT("REPRESENT","대표 사진"),
    //기본 리스트 이미지들
    NORMAL("NORMAL","일반 사진");

    private final String value;
    private final String name;
}

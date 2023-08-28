package com.example.matchdomain.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectKind {
    DOG("강아지"),
    CHILDREN("어린이"),
    YOUTH("청년"),
    WOMEN("여성"),
    ELDER("어르신"),
    DISABLED("장애인"),
    SOCIAL("우리 사회"),
    EARTH("지구촌"),
    NEIGHBOR("이웃"),
    ANIMAL("동물"),
    ENVIRONMENT("환경");
    private final String value;
}

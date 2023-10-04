package com.example.matchdomain.project.entity;

import com.example.matchdomain.donation.entity.RegularStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ProjectKind {
    CHILDREN("CHILDREN","어린이"),
    YOUTH("YOUTH","청년"),
    WOMEN("WOMEN","여성"),
    ELDER("ELDER","어르신"),
    DISABLED("DISABLED","장애인"),
    SOCIAL("SOCIAL","우리 사회"),
    EARTH("EARTH","지구촌"),
    NEIGHBOR("NEIGBOR","이웃"),
    ANIMAL("ANIMAL","동물"),
    ENVIRONMENT("ENVIRONMENT","환경");
    private final String value;
    private final String name;

    @JsonCreator(mode=JsonCreator.Mode.DELEGATING)
    public static ProjectKind get(String value) {
        return Arrays.stream(values())
                .filter(type -> type.getValue().equals(value))
                .findAny()
                .orElse(null);
    }
}

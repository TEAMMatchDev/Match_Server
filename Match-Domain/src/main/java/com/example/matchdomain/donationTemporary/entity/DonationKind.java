package com.example.matchdomain.donationTemporary.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DonationKind {
    DOG("DOG", "유기동물"),
    CHILD("CHILD", "가정폭력 피해 아동"),
    OCEAN("OCEAN","바다"),
    VISUALLY_IMPAIRED("VISUALLY_IMPAIRED","시각장애인의 안전");
    private final String value;
    private final String name;

    @JsonCreator(mode= JsonCreator.Mode.DELEGATING)
    public static DonationKind get(String value) {
        return Arrays.stream(values())
                .filter(type -> type.getValue().equals(value))
                .findAny()
                .orElse(null);
    }
}

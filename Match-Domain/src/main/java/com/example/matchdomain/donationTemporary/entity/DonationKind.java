package com.example.matchdomain.donationTemporary.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DonationKind {
    DOG("DOG", "유기동물"),
    CHILD("CHILD", "가정폭력 피해 아동"),
    OCEAN("OCEAN","바다"),
    VISUALLY_IMPAIRED("VISUALLY_IMPAIRED","시각장애인의 안전");
    private final String value;
    private final String name;
}

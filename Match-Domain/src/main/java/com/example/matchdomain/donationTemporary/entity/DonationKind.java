package com.example.matchdomain.donationTemporary.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DonationKind {
    DOG("DOG"),
    CHILD("CHILD"),
    OCEAN("OCEAN"),
    VISUALLY_IMPAIRED("VISUALLY_IMPAIRED");

    private final String value;
}

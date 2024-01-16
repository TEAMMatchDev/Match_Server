package com.example.matchdomain.donation.entity.flameEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlameImage {
    NORMAL_IMG("https://d331gpen6ndprr.cloudfront.net/flame.png"),
    TUTORIAL_IMG("https://d331gpen6ndprr.cloudfront.net/tutorial_flame.png");
    private final String img;
}

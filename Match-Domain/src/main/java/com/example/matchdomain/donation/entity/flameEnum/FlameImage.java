package com.example.matchdomain.donation.entity.flameEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlameImage {
    NORMAL_IMG("https://match-image.s3.ap-northeast-2.amazonaws.com/image+56.jpg");
    private final String img;
}

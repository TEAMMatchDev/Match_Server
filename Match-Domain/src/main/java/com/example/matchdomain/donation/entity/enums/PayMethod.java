package com.example.matchdomain.donation.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayMethod {
    CARD("CARD","신용카드"),
    VBANK("VBANK","카드"),
    NAVER_PAY("naverpay","네이버 페이"),
    KAKAO_PAY("kakaopay","카카오 페이"),
    PAYCO("payco","페이코"),
    SSGPAY("ssgpay","쓱페이"),
    SAMSUNG_PAY("samsungpay","삼성페이"),
    TUTORIAL("TUTORIAL","튜토리얼");

    private final String value;
    private final String name;
}

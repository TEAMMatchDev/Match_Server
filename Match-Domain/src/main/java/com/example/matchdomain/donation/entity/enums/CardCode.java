package com.example.matchdomain.donation.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardCode {
    BC("BC카드", "361"),
    GWANGJU("광주카드", "364"),
    SAMSUNG("삼성카드", "365"),
    SHINHAN("신한카드", "366"),
    HYUNDAI("현대카드", "367"),
    LOTTE("롯데카드", "368"),
    SUHYUP("수협카드", "369"),
    CITY("씨티카드", "370"),
    NH("NH카드", "371"),
    JEONBUK("전북카드", "372"),
    JEJU("제주카드", "373"),
    HANA("하나SK카드", "374"),
    KB("KB국민카드", "381"),
    KDB("KDB산업은행카드", "002"),
    WOORI("우리카드", "041"),
    SAEMAUL("새마을금고카드", "045"),
    SHINHYUP("신협카드", "048"),
    UCEHGUG("우체국", "071"),
    KBANK("케이뱅크카드", "089"),
    KAKAOBANK("카카오뱅크카드", "090");

    private final String name;
    private final String code;

    public static CardCode getNameByCode(String code) {
        for (CardCode card : values()) {
            if (card.code.equals(code)) {
                return card;
            }
        }
        return null;
    }
}

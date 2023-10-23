package com.example.matchdomain.donation.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CardCode {
    BC("BC카드", "361"),
    광주카드("광주카드", "364"),
    삼성카드("삼성카드", "365"),
    신한카드("신한카드", "366"),
    현대카드("현대카드", "367"),
    롯데카드("롯데카드", "368"),
    수협카드("수협카드", "369"),
    씨티카드("씨티카드", "370"),
    NH카드("NH카드", "371"),
    전북카드("전북카드", "372"),
    제주카드("제주카드", "373"),
    하나SK카드("하나SK카드", "374"),
    KB국민카드("KB국민카드", "381"),
    KDB산업은행카드("KDB산업은행카드", "2"),
    우리카드("우리카드", "41"),
    새마을금고카드("새마을금고카드", "45"),
    신협카드("신협카드", "48"),
    우체국("우체국", "71"),
    케이뱅크카드("케이뱅크카드", "89"),
    카카오뱅크카드("카카오뱅크카드", "90");

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

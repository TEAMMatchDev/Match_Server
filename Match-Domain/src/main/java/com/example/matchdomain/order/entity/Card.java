package com.example.matchdomain.order.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Card {
    BC("01", "비씨", "BC"),
    KB_KOOKMIN("02", "KB국민", "KB Kookmin"),
    KEB_BANK("03", "하나(외환)", "KEB Bank"),
    SAMSUNG("04", "삼성", "SAMSUNG"),
    SHINHAN("06", "신한", "SHINHAN"),
    HYUNDAI("07", "현대", "HYUNDAI"),
    LOTTE("08", "롯데", "LOTTE"),
    CITI("11", "씨티", "CITI"),
    NH("12", "NH채움", "NH"),
    SUHYUP("13", "수협", "SUHYUP"),
    SHINHYUP("14", "신협", "SHINHYUP"),
    WOORI("15", "우리", "WOORI"),
    HANA_SK("16", "하나", "HANA SK"),
    KWANGJU("21", "광주", "KWANGJU"),
    JEONBUK("22", "전북", "JEONBUK"),
    JEJU("23", "제주", "JEJU"),
    KDB_CAPITAL("24", "산은캐피탈", "KDB Capital"),
    VISA("25", "해외비자", "VISA"),
    MASTER("26", "해외마스터", "MASTER"),
    DINERS("27", "해외다이너스", "DINERS"),
    AMEX("28", "해외AMX", "AMEX"),
    JCB("29", "해외JCB", "JCB"),
    SK_OKCashBag("31", "SK-OKCashBag", "SK-OKCashBag"),
    POST("32", "우체국", "Post"),
    SAVINGS_BANK("33", "저축은행", "Savings Bank"),
    UNIONPAY("34", "은련", "UnionPay"),
    MG("35", "새마을금고", "MG"),
    KDB_BANK("36", "KDB산업", "KDB Bank"),
    KAKAO_BANK("37", "카카오뱅크", "Kakao Bank"),
    KBANK("38", "케이뱅크", "KBank"),
    PAYCO("39", "페이코포인트", "PAYCO"),
    KAKAO("40", "카카오머니", "KAKAO"),
    SSG("41", "SSG머니", "SSG"),
    NAVER("42", "네이버포인트", "NAVER");

    private final String code;
    private final String kr;
    private final String en;
}

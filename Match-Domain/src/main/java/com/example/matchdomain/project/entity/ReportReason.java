package com.example.matchdomain.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportReason {
    PROMOTION("PROMOTION","영리,홍보목적"),
    ILLEGAL("ILLEGAL","불법졍뵤"),
    LEWDNESS("LEWDNESS","음란,청소년 유해"),
    TERM_ABUSE("TERM_ABUSE","욕설,비방,차별,혐오"),
    PRSNL_INFRM("PRSNL_INFRM","개인 정보 노출, 거래"),
    PAPERING("PAPERING","도배,스팸"),
    OTHER("OTHER","기타");

    private final String value;
    private final String name;
}

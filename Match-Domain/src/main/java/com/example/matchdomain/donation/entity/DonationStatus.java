package com.example.matchdomain.donation.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DonationStatus {
    //진행중
    PROCEEDING("PROCEEDING"),
    //시작 전
    BEFORE_START("BEFORE_START"),
    //마감
    DEADLINE("DEADLINE");

    private final String value;
}

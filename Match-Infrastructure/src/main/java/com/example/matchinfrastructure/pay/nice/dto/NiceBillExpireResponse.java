package com.example.matchinfrastructure.pay.nice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class NiceBillExpireResponse {
    private String resultCode;

    private String resultMsg;

    private String tid;

    private String bid;

    private String authDate;
}

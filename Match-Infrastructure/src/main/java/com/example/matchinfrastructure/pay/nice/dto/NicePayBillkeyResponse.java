package com.example.matchinfrastructure.pay.nice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NicePayBillkeyResponse {
    private String resultCode;

    private String resultMsg;

    private String tid;

    private String orderId;

    private String bid;

    private String authDate;

    private String cardCode;

    private String cardName;
}

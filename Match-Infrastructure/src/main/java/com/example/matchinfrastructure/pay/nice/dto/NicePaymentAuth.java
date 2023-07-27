package com.example.matchinfrastructure.pay.nice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NicePaymentAuth {

    private String resultCode;

    private String resultMsg;

    private String tid;

    private String cancelledTid;

    private String orderId;

    private String ediDate;

    private String signature;

    private String status;

    private String paidAt;

    private String failedAt;

    private String cancelledAt;

    private String payMethod;

    private int amount;

    private int balanceAmt;

    private String goodsName;

    private String mallReserved;

    private boolean useEscrow;

    private String currency;

    private String channel;

    private String approveNo;

    private String buyerName;

    private String buyerTel;

    private String buyerEmail;

    private String receiptUrl;

    private String mallUserId;

    private boolean issuedCashReceipt;

    private String coupon;

    private Card card;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Card {
        private String cardCode;

        private String cardName;

        private String cardNum;

        private int cardQuota;

        private boolean isInterestFree;

        private String cardType;

        private boolean canPartCancel;

        private String acquCardCode;

        private String acquCardName;

    }

    private String vbank;

    private String cancels;

    private String cashReceipts;
}

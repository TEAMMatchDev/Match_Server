package com.example.matchinfrastructure.pay.nice.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class NiceBillOkResponse {

    private Card card;
    private boolean issuedCashReceipt;
    private String receiptUrl;
    private String currency;
    private boolean useEscrow;
    private String goodsName;
    private int balanceAmt;
    private int amount;
    private String payMethod;
    private String cancelledAt;
    private String failedAt;
    private String paidAt;
    private String status;
    private String ediDate;
    private String orderId;
    private String tid;
    private String resultMsg;
    private String resultCode;

    @Getter
    @Setter
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Card {
        private String acquCardName;
        private String acquCardCode;
        private boolean canPartCancel;
        private String cardType;
        private boolean isInterestFree;
        private int cardQuota;
        private String cardNum;
        private String cardName;
        private String cardCode;
    }
}

package com.example.matchinfrastructure.pay.nice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    private Card card;
    private Bank bank;
    private Vbank vbank;
    private List<Coupon> coupon;

    private List<Cancels> cancels;
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Cancels{
        private String tid;

        private int amount;

        private String cancelledAt;

        private String reason;

        private String receiptUrl;

        private int couponAmt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coupon {
        private int couponAmt;
    }


    private CashReceipts cashReceipts;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CashReceipts{
        private String receiptTid;

        private String orgTid;

        private String status;

        private int amount;

        private int taxFreeAmt;

        private String receiptType;

        private String issueNo;

        private String receiptUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Bank{
        private String bankCode;

        private String bankName;
    }




    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Vbank{
        private String vbankCode;
        private String vbankName;
        private String vbankNumber;
        private String vbankExpDate;
        private String vbankHolder;

    }
}

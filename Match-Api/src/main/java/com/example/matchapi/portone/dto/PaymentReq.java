package com.example.matchapi.portone.dto;

import lombok.*;

public class PaymentReq {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValidatePayment{
        private String impUid;
        private String orderId;
        private int amount;
        private String payMethod;
    }
}

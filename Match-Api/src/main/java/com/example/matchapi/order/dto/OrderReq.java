package com.example.matchapi.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

public class OrderReq {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderDetail {
        private String signature;
        private String authToken;
        private String mallReserved;
        private String amount;
        private String orderId;
        private String clientId;
        private String tid;
        private String authResultMsg;
        private String authResultCode;
    }
}

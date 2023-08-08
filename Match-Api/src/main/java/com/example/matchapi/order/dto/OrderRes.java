package com.example.matchapi.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class OrderRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "04-03💸 정기 결제용 카드 조회 API Request")
    public static class UserBillCard {
        private Long id;

        private String cardCode;

        private String cardName;

        private String cardNo;
    }
}

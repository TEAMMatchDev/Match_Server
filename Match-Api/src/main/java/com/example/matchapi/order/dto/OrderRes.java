package com.example.matchapi.order.dto;

import com.example.matchdomain.donation.entity.enums.RegularStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class OrderRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "04-03💸 정기 결제용 카드 조회 API Response")
    public static class UserBillCard {
        private Long id;

        private String cardCode;

        private String cardName;

        private String cardNo;

        private String cardAbleStatus;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "04-06💸 후원자 정보조회 API  API Response")
    public static class UserDetail {
        private String name;

        private String birthDay;

        private String phoneNumber;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "기부 API API Response")
    public static class CompleteDonation {
        private String username;

        private String title;

        private String usages;

        private String amount;

        private String regularStatus;
    }
}

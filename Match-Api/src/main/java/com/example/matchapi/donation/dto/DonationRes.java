package com.example.matchapi.donation.dto;

import com.example.matchdomain.donation.entity.DonationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class DonationRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationList {
        private Long donationId;

        private String donationDate;

        private String projectName;

        private String regularDate;

        private String donationStatus;

        private String regularStatus;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FlameList {
        private Long donationId;

        private String flameName;

        private String donationStatus;

        private Long amount;

        private String createdAt;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationCount {
        @Schema(description = "후원 집행 전 갯수", required = true, example = "후원 집행 전 갯수")
        private int beforeCnt;

        @Schema(description = "후원 집행 진행 중 갯수", required = true, example = "후원 집행 진행 중 갯수")
        private int underCnt;

        @Schema(description = "후원 집행 진행 중 갯수", required = true, example = "후원 집행 진행 중 갯수")
        private int successCnt;
    }
}

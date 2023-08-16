package com.example.matchapi.donation.dto;

import com.example.matchdomain.donation.entity.DonationStatus;
import lombok.*;

public class DonationRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationList {
        private Long donationId;

        private String donationStatus;

        private String flameName;

        private String regularStatus;
    }
}

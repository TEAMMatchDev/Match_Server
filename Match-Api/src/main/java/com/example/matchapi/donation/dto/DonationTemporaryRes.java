package com.example.matchapi.donation.dto;

import lombok.*;

public class DonationTemporaryRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfo {
        private String name;

        private String phoneNumber;

        private String email;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationList {
        private String name;

        private String amount;

        private String donationDate;
    }
}

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
}

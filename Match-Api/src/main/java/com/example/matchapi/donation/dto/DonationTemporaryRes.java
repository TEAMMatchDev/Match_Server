package com.example.matchapi.donation.dto;

import com.example.matchdomain.donationTemporary.entity.Deposit;
import com.example.matchdomain.donationTemporary.entity.DonationKind;
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

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationRequestAdminList {
        private Long donationRequestId;

        private String username;

        private String phoneNumber;

        private String email;

        private String alarmMethod;

        private String donationKind;

        private String deposit;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationDetail {
        private Long donationRequestId;

        private String username;

        private String phoneNumber;

        private String email;

        private String alarmMethod;

        private String donationKind;

        private String deposit;
    }
}

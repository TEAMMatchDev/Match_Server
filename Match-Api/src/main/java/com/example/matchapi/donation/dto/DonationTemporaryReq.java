package com.example.matchapi.donation.dto;

import com.example.matchdomain.donationTemporary.entity.AlarmMethod;
import com.example.matchdomain.donationTemporary.entity.DonationKind;
import lombok.*;

public class DonationTemporaryReq {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationInfo {
        private String username;

        private String phoneNumber;

        private AlarmMethod alarmMethod;

        private DonationKind donationKind;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationDeposit {
        private Long donationRequestId;

        private String name;

        private int amount;
    }
}

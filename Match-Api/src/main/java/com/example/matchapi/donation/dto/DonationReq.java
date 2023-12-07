package com.example.matchapi.donation.dto;

import lombok.*;

import java.util.List;

public class DonationReq {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class EnforceDonation {
        private Long projectId;

        private List<String> item;

        private List<Long> donationUserLists;

        private List<SomeExecution> someExecutions;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class SomeExecution{
        private Long donationUserId;

        private Long amount;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Tutorial {
        private Long projectId;
    }
}

package com.example.matchapi.donation.dto;

import lombok.*;

import java.util.List;

public class DonationReq {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnforceDonation {
        private Long projectId;
        private List<Long> donationUserLists;

    }
}

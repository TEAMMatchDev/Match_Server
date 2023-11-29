package com.example.matchapi.review.dto;

import com.example.matchdomain.donation.entity.enums.RegularStatus;
import lombok.*;

public class ReviewRes {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PopUpInfo {
        private Long executionId;

        private String title;

        private String regularStatus;
    }
}

package com.example.adminapi.project.dto;

import lombok.*;

public class ProjectRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectList {
        private Long projectId;

        private String projectName;

        private String usages;

        private int totalDonationCnt;

        private int totalAmount;
    }
}

package com.example.matchapi.banner.dto;

import java.time.LocalDateTime;

import lombok.*;

public class BannerReq {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class BannerUpload {
        private Long eventId;

        private String contentsUrl;

        private String name;

        private LocalDateTime startDate;

        private LocalDateTime endDate;
    }


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @ToString
    @NoArgsConstructor
	public static class BannerPatchDto {
        private String name;

        private String contentsUrl;

        private LocalDateTime startDate;

        private LocalDateTime endDate;

        private boolean isEditImage;
	}
}

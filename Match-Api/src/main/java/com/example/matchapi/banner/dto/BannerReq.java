package com.example.matchapi.banner.dto;

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
    }
}

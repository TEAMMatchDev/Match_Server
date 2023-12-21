package com.example.matchapi.banner.dto;

import com.example.matchdomain.banner.enums.BannerType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BannerRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BannerList implements Serializable {
        private Long bannerId;

        private BannerType bannerType;

        private String bannerImg;

        private Long eventId;

        private String contentsUrl;


    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BannerAdminListDto {
        private Long bannerId;

        private BannerType bannerType;

        private String name;

        private String contentsUrl;

        private String bannerImg;

        private Long eventId;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startDate;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime endDate;
    }
}

package com.example.matchapi.banner.dto;

import com.example.matchdomain.banner.enums.BannerType;
import lombok.*;

import java.io.Serializable;

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
}

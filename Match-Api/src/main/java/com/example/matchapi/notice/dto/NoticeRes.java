package com.example.matchapi.notice.dto;

import lombok.*;

public class NoticeRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NoticeList {
        private Long noticeId;

        private String noticeType;

        private String title;

        private String noticeDate;
    }
}

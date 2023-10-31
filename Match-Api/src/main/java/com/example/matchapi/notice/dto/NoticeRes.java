package com.example.matchapi.notice.dto;

import com.example.matchdomain.common.model.ContentsType;
import lombok.*;

import java.io.Serializable;
import java.util.List;

public class NoticeRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NoticeList implements Serializable {
        private Long noticeId;

        private String noticeType;

        private String title;

        private String noticeDate;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NoticeDetail {
        private NoticeList noticeInfo;

        private List<NoticeContents> noticeContents;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NoticeContents{
        private Long contentId;

        private ContentsType contentsType;

        private String contents;
    }
}

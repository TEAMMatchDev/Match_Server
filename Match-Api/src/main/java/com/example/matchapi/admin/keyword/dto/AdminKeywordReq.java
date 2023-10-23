package com.example.matchapi.admin.keyword.dto;

import lombok.*;

import java.io.Serializable;

public class AdminKeywordReq {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KeywordUpload implements Serializable {
        private String searchKeyword;

        private int priority;
    }
}

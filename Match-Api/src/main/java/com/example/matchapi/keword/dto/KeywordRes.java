package com.example.matchapi.keword.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class KeywordRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KeywordList {
        private Long keywordId;

        @Schema(name = "priority", description = "우선 순위 입니다.")
        private int priority;

        private String keyword;
    }
}

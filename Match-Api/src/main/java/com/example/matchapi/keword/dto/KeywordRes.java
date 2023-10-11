package com.example.matchapi.keword.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

public class KeywordRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KeywordList implements Serializable {
        private static final long serialVersionUID = 12312312421412L;

        private Long keywordId;

        @Schema(name = "priority", description = "우선 순위 입니다.")
        private int priority;

        private String keyword;
    }
}

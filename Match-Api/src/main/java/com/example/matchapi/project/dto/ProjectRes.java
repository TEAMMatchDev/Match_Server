package com.example.matchapi.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class ProjectRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectList {
        @Schema(description = "projectId 값", required = true, example = "1")
        private Long projectId;
        @Schema(description = "프로젝트 대표 이미 URL", required = true, example = "imgUrl")
        private String imgUrl;
        @Schema(description = "프로젝트 이름", required = true, example = "title")
        private String title;
        @Schema(description = "프로젝트 사용처", required = true, example = "프로젝트 사용처")
        private String usages;
    }
}

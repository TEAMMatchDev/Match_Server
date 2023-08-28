package com.example.matchdomain.project.dto;

import lombok.*;

public class ProjectDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ProjectImageDto {
        private Long id;
        private String projectName;
        private String url;

    }
}

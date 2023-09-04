package com.example.matchapi.project.dto;

import com.example.matchcommon.annotation.Enum;
import com.example.matchdomain.donation.entity.RegularStatus;
import com.example.matchdomain.project.entity.ProjectKind;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ProjectReq {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Project{
        @NotEmpty(message = "프로젝트 이름을 입력해주세요")
        @Schema(description = "프로젝트 이름", required = true, example = "tbt")
        private String projectName;
        @NotEmpty(message = "프로젝트 사용처를 입력해주세요")
        @Schema(description = "프로젝트 사용처", required = true, example = "The Better Tomorrow")
        private String usages;
        @NotEmpty(message = "프로젝트 설명을 입력해주세요")
        @Schema(description = "프로젝트 설명", required = true, example = "프로젝트 설명")
        private String detail;
        @NotNull(message = "프로젝트 기부 모금 종류를 입력해주세요")
        @Enum(message = "ONE_TIME, REGULAR 둘 중 하나를 입력해 주세요 ONE_TIME = 일회성 후원, REGULAR = 정기 후원")
        @Schema(description = "프로젝트 기부 모금 종류 ONE_TIME, REGULAR 둘 중 하나를 입력해 주세요", required = true, example = "ONE_TIME")
        private RegularStatus regularStatus;
        @Schema(description = "프로젝트 기부 시작 날짜입니다. pattern = yyyy-MM-dd'T'HH:mm:ss", required = true, example = "ONE_TIME")
        @NotNull(message = "기부 시작 날짜입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime startDate;
        @Schema(description = "프로젝트기부 종료 날짜입니다. pattern = yyyy-MM-dd'T'HH:mm:ss", required = true, example = "ONE_TIME")
        @NotNull(message = "기부 프로젝트 종류 날자를 입력해주세요")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime endDate;
        @Enum(message = "DOG, CHILDREN,YOUTH,WOMEN, ELDER, DISABLED, SOCIAL, EARTH, NEIGHBOR, ANIMAL, ENVIRONMENT 중 입력해주세요")
        private ProjectKind projectKind;


    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModifyProject {
        @NotEmpty(message = "프로젝트 이름을 입력해주세요")
        @Schema(description = "프로젝트 이름", required = true, example = "tbt")
        private String projectName;
        @NotEmpty(message = "프로젝트 사용처를 입력해주세요")
        @Schema(description = "프로젝트 사용처", required = true, example = "The Better Tomorrow")
        private String usages;
        @NotEmpty(message = "프로젝트 설명을 입력해주세요")
        @Schema(description = "프로젝트 설명", required = true, example = "프로젝트 설명")
        private String detail;
        @NotNull(message = "프로젝트 기부 모금 종류를 입력해주세요")
        @Enum(message = "ONE_TIME, REGULAR 둘 중 하나를 입력해 주세요 ONE_TIME = 일회성 후원, REGULAR = 정기 후원")
        @Schema(description = "프로젝트 기부 모금 종류 ONE_TIME, REGULAR 둘 중 하나를 입력해 주세요", required = true, example = "ONE_TIME")
        private RegularStatus regularStatus;
        @Schema(description = "프로젝트 기부 시작 날짜입니다. pattern = yyyy-MM-dd'T'HH:mm:ss", required = true, example = "ONE_TIME")
        @NotNull(message = "기부 시작 날짜입니다.")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime startDate;
        @Schema(description = "프로젝트기부 종료 날짜입니다. pattern = yyyy-MM-dd'T'HH:mm:ss", required = true, example = "ONE_TIME")
        @NotNull(message = "기부 프로젝트 종류 날자를 입력해주세요")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime endDate;
        @Enum(message = "DOG, CHILDREN,YOUTH,WOMEN, ELDER, DISABLED, SOCIAL, EARTH, NEIGHBOR, ANIMAL, ENVIRONMENT 중 입력해주세요")
        private ProjectKind projectKind;
    }
}

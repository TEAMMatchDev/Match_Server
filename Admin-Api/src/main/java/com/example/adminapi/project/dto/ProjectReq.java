package com.example.adminapi.project.dto;

import com.example.matchcommon.annotation.Enum;
import com.example.matchdomain.donation.entity.RegularStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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

        /*
        @Schema(description = "프로젝트 기부 시작 날짜입니다. pattern = yyyyMMddHHmmsss", required = true, example = "ONE_TIME")
        @NotNull(message = "기부 시작 날짜입니다.")
        @DateTimeFormat(pattern="yyyyMMddHHmmss")
        private LocalDateTime startDate;
        @Schema(description = "프로젝트기부 종료 날짜입니다. pattern = yyyyMMddHHmmsss", required = true, example = "ONE_TIME")
        @NotNull(message = "기부 프로젝트 종류 날자를 입력해주세요")
        @DateTimeFormat(pattern="yyyyMMddHHmmss")
        private LocalDateTime endDate;

         */
    }

    public static class ModifyProjectStatus {
    }
}

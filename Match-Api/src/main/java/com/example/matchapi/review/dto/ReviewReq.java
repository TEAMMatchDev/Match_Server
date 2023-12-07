package com.example.matchapi.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class ReviewReq {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReviewUpload{
        @NotNull(message = "id 값을 입력해주세요")
        @Schema(description = "executionId 값")
        private Long executionId;

        @Schema(description = "후원 경험")
        @NotNull(message = "후원 경험을 입력해주세요")
        private int donation;

        @Schema(description = "투명성")
        @NotNull(message = "투명성을 입력해주세요")
        private int transparency;

        @Schema(description = "정보 제공")
        @NotNull(message = "정보 제공을 입력해주세요")
        private int information;

        @Schema(description = "함께 한 후원 경험")
        private String comment;
    }
}

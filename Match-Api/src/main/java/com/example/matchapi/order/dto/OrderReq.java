package com.example.matchapi.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderReq {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "04-01💸 기부 결제 요청 API Request")
    public static class OrderDetail {
        @NotNull(message = "기부 프로젝트 ID를 입력해주세요")
        @Schema(description = "기부 해당 프로젝트 ID", example = "1")
        private Long projectId;
        @NotEmpty(message = "나이스 페이먼츠 고유 거래 키 값을 입력해주세요")
        @Schema(description = "결제 고유 거래 KEY 값", example = "UT0000113m01012110051656331001")
        private String tid;
        @NotNull(message = "기부 결제 금액을 입력해주세요.")
        @Schema(description = "결제 금액", example = "1000")
        private Long amount;
    }
}

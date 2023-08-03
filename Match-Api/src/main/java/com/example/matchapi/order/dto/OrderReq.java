package com.example.matchapi.order.dto;

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
        @NotEmpty(message = "나이스 페이먼츠 고유 거래 키 값을 입력해주세요")
        @Schema(description = "결제 고유 거래 KEY 값", example = "UT0000113m01012110051656331001")
        private String tid;
        @NotNull(message = "기부 결제 금액을 입력해주세요.")
        @Schema(description = "결제 금액", example = "1000")
        private Long amount;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "04-02💸 정기 결제용 카드 등록 API Request")
    public static class RegistrationCard {
        @NotEmpty(message = "카드 번호를 입력해주세요")
        @Schema(description = "카드 번호",example = "1234567890123456")
        private String cardNo;

        @NotEmpty(message = "카드 만료 년도를 입력해주세요")
        @Schema(description = "카드 만료 년도 YY", example = "29")
        private String expYear;

        @NotEmpty(message = "카드 만료 달을 입력해주세요")
        @Schema(description = "카드 만료 달 MM",example = "06")
        private String expMonth;

        @NotEmpty(message = "카드 계약옵션을 입력해주세요")
        @Schema(description = "계약옵션 생년월일(6자리) yyMMdd, 법인카드인 경우 사업자번호 10자리 ", example = "생년월일(6자리) yyMMdd, 법인카드인 경우 사업자번호 10자리")
        private String idNo;

        @NotEmpty(message = "카드 비밀번호 두자리를 입력해주세요")
        @Schema(description = "카드 비밀번호 앞 두자리" ,example = "12")
        private String cardPw;


    }
}

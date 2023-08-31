package com.example.adminapi.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class UserRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description ="ADMIN 01-01 로그인 후 토큰 발급 API Response")
    public static class UserToken{
        @Schema(description = "userId 값", required = true, example = "1")
        private Long userId;
        @Schema(description = "액세스 토큰", required = true, example = "asdkjanwjkldnjk----")
        private String accessToken;
        @Schema(description = "리프레쉬 토큰", required = true, example = "asdkjanwjkldnjk----")
        private String refreshToken;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Token{
        private String accessToken;

        private String refreshToken;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpInfo {
        private Long totalUserCnt;

        private Long oneDayUserCnt;

        private Long weekUserCnt;

        private Long monthUserCnt;
    }
}

package com.example.matchapi.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class UserRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description ="01-02,03🔑 로그인 후 토큰 발급 API Response")
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
    public static class UserAddress {
        private Long id;
        private Long userId;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description ="01-04🔑 문자인증 API Response")
    public static class Sms {
        @Schema(description = "회원가입 인증 번호", required = true, example = "241244")
        private String number;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description ="02-01👤 문자인증 API Response")
    public static class MyPage {
        @Schema(description = "userId", required = true, example = "userId 값")
        private Long userId;
        @Schema(description = "유저 이메일", required = true, example = "match123@gmail.com")
        private String email;
        @Schema(description = "유저 이름", required = true, example = "임현우")
        private String name;
        @Schema(description = "유저 전화번호", required = true, example = "01041231434")
        private String phone;
        @Schema(description = "유저 성별", required = true, example = "남자, 여자, 알 수 없음")
        private String gender;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @Schema(description = "유저 생일", required = true, example = "19990413")
        private String birthDate;
    }
}

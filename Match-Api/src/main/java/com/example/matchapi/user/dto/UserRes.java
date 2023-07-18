package com.example.matchapi.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class UserRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-02,03🔑 로그인 후 토큰 발급 API Response")
    public static class UserToken{
        @ApiModelProperty(notes = "userId 값", required = true, example = "1")
        private Long userId;
        @ApiModelProperty(notes = "액세스 토큰", required = true, example = "asdkjanwjkldnjk----")
        private String accessToken;
        @ApiModelProperty(notes = "리프레쉬 토큰", required = true, example = "asdkjanwjkldnjk----")
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
    @ApiModel("01-04🔑 문자인증 API Response")
    public static class Sms {
        @ApiModelProperty(notes = "회원가입 인증 번호", required = true, example = "241244")
        private String number;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("02-01👤 문자인증 API Response")
    public static class MyPage {
        @ApiModelProperty(notes = "userId", required = true, example = "userId 값")
        private Long userId;
        @ApiModelProperty(notes = "유저 이메일", required = true, example = "match123@gmail.com")
        private String email;
        @ApiModelProperty(notes = "유저 이름", required = true, example = "임현우")
        private String name;
        @ApiModelProperty(notes = "유저 전화번호", required = true, example = "01041231434")
        private String phone;
        @ApiModelProperty(notes = "유저 성별", required = true, example = "남자, 여자, 알 수 없음")
        private String gender;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @ApiModelProperty(notes = "유저 생일", required = true, example = "19990413")
        private String birthDate;
    }
}

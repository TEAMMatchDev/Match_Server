package com.example.matchapi.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;

public class UserReq {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ApiModel(value = "01-02,03🔑 소셜 로그인 토큰 API Request")
    public static class SocialLoginToken{
        @ApiModelProperty(notes = "소셜 액세스 토큰", required = true, example = "asdkjanwjkldnjk----")
        @NotEmpty(message = "토큰을 입력해주세요")
        private String accessToken;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("01-04🔑 문자인증 API Request")
    public static class Sms {
        @ApiModelProperty(notes = "전화번호 입력", required = true, example = "01012345678")
        @NotEmpty(message = "전화번호를 입력해주세요")
        @Size(min = 11, max = 11, message = "전화번호는 11자리 이어야 합니다.")
        @Pattern(regexp = "^01(?:0|1|[6-9])[0-9]{7,8}$", message = "전화번호 형식에 맞지 않습니다. 01012345678 '-' 를 제외하고 입력해주세요. ")
        private String phone;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("01-05🔑 회원가입 API Request")
    public static class SignUpUser {
        @Email
        @NotEmpty (message = "이메일을 입력해주세요")
        @ApiModelProperty(notes="이메일",required = true,example = "match123@gmail.com")
        private String email;
        @NotEmpty (message = "비밀번호를 입력해주세요")
        @ApiModelProperty(notes="비밀번호",required = true,example = "1234")
        private String password;
        @NotEmpty (message = "이름을 입력해주세요")
        @ApiModelProperty(notes="이름",required = true,example = "match123")
        private String name;
        @NotEmpty(message = "전화번호를 입력해주세요")
        @Size(min = 11, max = 11, message = "전화번호는 11자리 이어야 합니다.")
        @Pattern(regexp = "^01(?:0|1|[6-9])[0-9]{7,8}$", message = "전화번호 형식에 맞지 않습니다. 01012345678 '-' 를 제외하고 입력해주세요. ")
        @ApiModelProperty(notes="전화번호",required = true,example = "0101234567")
        private String phone;
        @NotEmpty (message = "성별을 입력해주세요")
        @ApiModelProperty(notes="성별",required = true,example = "남자는 남자 여자는 여자")
        private String gender;
        @NotEmpty (message = "생년월일을 입력해주세요")
        @ApiModelProperty(notes="생일",required = true,example = "20200101")
        private String birthDate;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("01-05🔑 회원가입 핸드폰 번호 검증 API Request")
    public static class UserPhone {
        @NotEmpty(message = "전화번호를 입력해주세요")
        @Size(min = 11, max = 11, message = "전화번호는 11자리 이어야 합니다.")
        @Pattern(regexp = "^01(?:0|1|[6-9])[0-9]{7,8}$", message = "전화번호 형식에 맞지 않습니다. 01012345678 '-' 를 제외하고 입력해주세요. ")
        @ApiModelProperty(notes="전화번호",required = true,example = "0101234567")
        private String phone;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("01-05-01🔑 회원가입 이메일 검증 API Request")
    public static class UserEmail {
        @Email
        @ApiModelProperty(notes="이메일",required = true,example = "match123@gmail.com")
        private String email;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("01-06🔑 로그인 API Request")
    public static class LogIn {
        @Email
        @ApiModelProperty(notes="이메일",required = true,example = "match123@gmail.com")
        private String email;
        @NotEmpty (message = "비밀번호를 입력해주세요")
        @ApiModelProperty(notes="비밀번호",required = true,example = "1234")
        private String password;
    }
}

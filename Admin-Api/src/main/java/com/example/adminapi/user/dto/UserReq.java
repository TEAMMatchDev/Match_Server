package com.example.adminapi.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UserReq {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "ADMIN 01-01🔑 로그인 API Request")
    public static class LogIn {
        @Email
        @Schema(description ="유저 아이디",required = true,example = "match123@gmail.com")
        private String email;
        @NotEmpty(message = "비밀번호를 입력해주세요")
        @Schema(description ="비밀번호",required = true,example = "1234")
        private String password;
    }
}

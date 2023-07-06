package com.example.matchapi.user.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

public class UserReq {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @ApiModel(value = "01-02,03🔑 소셜 로그인 토큰 API Request")
    public static class SocialLoginToken{
        private String accessToken;
    }
}

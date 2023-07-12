package com.example.matchapi.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
}

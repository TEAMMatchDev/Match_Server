package com.example.matchapi.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserRes {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "01-02,03🔑 로그인 후 토큰 발급 API Response")
    public static class UserToken{
        @ApiModelProperty(notes = "액세스 토큰", required = true, example = "asdkjanwjkldnjk----")
        private String accessToken;

        @ApiModelProperty(notes = "리프레쉬 토큰", required = true, example = "asdkjanwjkldnjk----")
        private String refreshToken;
    }
}

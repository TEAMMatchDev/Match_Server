package com.example.matchapi.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
}

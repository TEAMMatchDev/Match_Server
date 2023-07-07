package com.example.matchinfrastructure.oauth.naver.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NaverUserInfoDto {
    private String result_code;
    private String message;
    private Response response;

    @Getter
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response{
        private String id;
        private String nickname;
        private String name;
        private String email;
        private String gender;
        private String age;
        private String birthday;
        private String profile_image;
        private String birthyear;
        private String mobile;
    }
}

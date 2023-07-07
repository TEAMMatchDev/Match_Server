package com.example.matchinfrastructure.oauth.naver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NaverTokenRes {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private int expires_in;
    private String error;
    private String error_description;

    public String getAccess_token() {
        return "Bearer "+access_token;
    }
}

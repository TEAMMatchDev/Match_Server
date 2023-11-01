package com.example.matchinfrastructure.oauth.apple.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppleUserRes {
    private String email;

    private String socialId;
}

package com.example.matchinfrastructure.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FCMNotificationRequestDto {
    private String title;

    private String body;

    private String token;
}

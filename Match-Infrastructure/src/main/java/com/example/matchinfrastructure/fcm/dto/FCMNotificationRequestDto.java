package com.example.matchinfrastructure.fcm.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class FCMNotificationRequestDto {
    private String title;

    private String body;

    private String token;
}

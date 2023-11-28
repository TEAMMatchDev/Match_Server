package com.example.matchinfrastructure.aligo.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateTokenRes {
    private int code;
    private String message;
    private String token;
    private String urlencode;
}

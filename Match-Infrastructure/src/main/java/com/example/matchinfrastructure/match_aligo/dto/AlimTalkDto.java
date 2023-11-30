package com.example.matchinfrastructure.match_aligo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlimTalkDto {
    private Long donationId;
    private String name;
    private String phone;
    private String usages;
    private String article;
}

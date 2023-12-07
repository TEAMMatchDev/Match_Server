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
    //사용처
    private String usages;
    //기부 품목
    private String article;
}

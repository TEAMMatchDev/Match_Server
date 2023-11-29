package com.example.matchinfrastructure.aligo.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class AlimTalkRes {
    private String type;
    private String mid;
    private float current;
    private float unit;
    private float total;
    private int scnt;
    private int fcnt;
}

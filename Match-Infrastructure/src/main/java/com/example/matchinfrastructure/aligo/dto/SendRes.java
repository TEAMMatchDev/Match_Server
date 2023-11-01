package com.example.matchinfrastructure.aligo.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SendRes {
    private Integer resultCode;
    private String message;
    private Integer msgId;
    private Integer successCnt;
    private Integer errorCnt;
    private String msgType;
}

package com.example.matchinfrastructure.pay.portone.dto.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortOneBillReq {
    private String pg;
    private String card_number;
    private String expiry;
    private String birth;
    private String pwd_2digit;
}

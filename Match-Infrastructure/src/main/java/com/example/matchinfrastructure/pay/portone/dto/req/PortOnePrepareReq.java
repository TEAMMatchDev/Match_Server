package com.example.matchinfrastructure.pay.portone.dto.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortOnePrepareReq {
    private String merchant_uid;

    private double amount;
}

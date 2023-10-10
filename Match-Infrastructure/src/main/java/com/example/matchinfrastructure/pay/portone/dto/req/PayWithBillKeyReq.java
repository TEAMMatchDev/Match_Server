package com.example.matchinfrastructure.pay.portone.dto.req;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayWithBillKeyReq {
    private String customer_uid;
    private String merchant_uid;
    private double amount;
    private String name;
}

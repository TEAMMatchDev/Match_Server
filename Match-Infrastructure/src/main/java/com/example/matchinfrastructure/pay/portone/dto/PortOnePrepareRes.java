package com.example.matchinfrastructure.pay.portone.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortOnePrepareRes {
    private String merchant_uid;

    private double amount;
}

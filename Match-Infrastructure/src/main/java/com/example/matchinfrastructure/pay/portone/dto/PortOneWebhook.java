package com.example.matchinfrastructure.pay.portone.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class PortOneWebhook {
    private String imp_uid;

    private String merchant_uid;

    private PaidStatus status;
}

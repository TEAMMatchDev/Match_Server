package com.example.matchinfrastructure.pay.nice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NicePayCancelRequest {
    private String reason;

    private String orderId;
}

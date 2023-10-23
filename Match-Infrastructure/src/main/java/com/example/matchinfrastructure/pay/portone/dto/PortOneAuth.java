package com.example.matchinfrastructure.pay.portone.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortOneAuth {
    private String access_token;
    private Long expired_at;
    private Long now;
}

package com.example.matchinfrastructure.pay.portone.dto.req;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortOneAuthReq {
    private String imp_key;

    private String imp_secret;
}

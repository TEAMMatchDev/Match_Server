package com.example.matchinfrastructure.pay.nice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NicePayRegistrationCardRequest {
    private String encData;

    private String orderId;

    private String encMode;
}

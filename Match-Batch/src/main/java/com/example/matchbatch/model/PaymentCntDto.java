package com.example.matchbatch.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCntDto {
    private int totalAmount;
    private int successCnt;
}

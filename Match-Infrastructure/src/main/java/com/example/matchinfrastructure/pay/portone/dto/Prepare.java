package com.example.matchinfrastructure.pay.portone.dto;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prepare {
    @SerializedName("merchant_uid")
    String merchant_uid;

    @SerializedName("amount")
    BigDecimal amount;
}

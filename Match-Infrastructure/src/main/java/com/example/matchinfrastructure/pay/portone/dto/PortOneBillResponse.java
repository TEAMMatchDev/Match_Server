package com.example.matchinfrastructure.pay.portone.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortOneBillResponse {
    private String customer_uid;
    private String pg_provider;
    private String pg_id;
    private String customer_id;
    private String card_name;
    private String card_code;
    private String card_number;
    private int card_type;
    private String customer_name;
    private String customer_tel;
    private String customer_email;
    private String customer_addr;
    private String customer_postcode;
    private int inserted;
    private int updated;
}

package com.example.matchinfrastructure.pay.portone.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class PortOneBillDeleteResponse {
    private String customer_uid; // 구매자의 결제 수단 식별 고유번호
    private String pg_provider;  // PG사 구분코드
    private String pg_id;        // PG사 상점아이디(MID)
    private String customer_id;  // 구매자 ID
    private String card_name;    // 빌링키 발급 한 카드명
    private String card_code;    // 카드사 코드
    private String card_number;  // 마스킹된 카드번호
    private Integer card_type;   // 카드유형
    private String customer_name; // 고객 성함
    private String customer_tel;  // 전화번호
    private String customer_email; // Email 주소
    private String customer_addr;  // 주소
    private String customer_postcode; // 우편번호
    private Integer inserted;     // 빌링키 발급 시각 UNIX timestamp
    private Integer updated;      // 빌링키 업데이트 시각 UNIX timestamp
}

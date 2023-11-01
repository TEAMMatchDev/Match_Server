package com.example.matchinfrastructure.pay.nice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class NiceBillOkRequest {

    //상점분담 무이자 false
    private boolean useShopInterest;
    //할부 개월
    private int cardQuota;
    //상품 이름
    private String goodsName;
    //결제금액
    private Long amount;
    //고유번호
    private String orderId;


}

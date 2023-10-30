package com.example.matchinfrastructure.pay.portone.convertor;

import com.example.matchcommon.annotation.Convertor;
import com.example.matchcommon.properties.PortOneProperties;
import com.example.matchinfrastructure.pay.portone.dto.req.PayWithBillKeyReq;
import com.example.matchinfrastructure.pay.portone.dto.req.PortOneBillReq;
import com.example.matchinfrastructure.pay.portone.dto.req.PortOnePrepareReq;
import lombok.RequiredArgsConstructor;

@Convertor
@RequiredArgsConstructor
public class PortOneConvertor {
    private final PortOneProperties portOneProperties;
    public PortOneBillReq convertToPortOneBill(String cardNo, String expiry, String idNo, String cardPw) {
        return PortOneBillReq
                .builder()
                .pg("nice."+portOneProperties.getBillmid())
                .card_number(cardNo)
                .expiry(expiry)
                .birth(idNo)
                .pwd_2digit(cardPw)
                .build();
    }

    public PayWithBillKeyReq convertPayWithBillKey(String bid, String orderId, Long amount, String projectName, String customerId) {
        return PayWithBillKeyReq
                .builder()
                .customer_uid(bid)
                .merchant_uid(orderId)
                .amount(amount)
                .name(projectName)
                .build();
    }

    public PortOnePrepareReq convertToRequestPrepare(String orderId, int amount) {
        return PortOnePrepareReq
                .builder()
                .merchant_uid(orderId)
                .amount(amount)
                .build();
    }
}

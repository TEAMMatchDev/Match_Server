package com.example.matchapi.order.convertor;

import com.example.matchapi.order.dto.OrderReq;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.DonationStatus;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchinfrastructure.pay.nice.dto.*;
import lombok.RequiredArgsConstructor;

import static java.lang.Integer.parseInt;

@Convertor
@RequiredArgsConstructor
public class OrderConvertor {
    private final OrderHelper orderHelper;
    public DonationUser donationUser(NicePaymentAuth nicePaymentAuth, Long id, OrderReq.OrderDetail orderDetail, Long projectId, String flameName, String inherenceNumber) {
        return DonationUser.builder()
                .userId(id)
                .projectId(projectId)
                .price(parseInt(String.valueOf(orderDetail.getAmount())))
                .tid(nicePaymentAuth.getTid())
                .orderId(nicePaymentAuth.getOrderId())
                .donationStatus(DonationStatus.EXECUTION_BEFORE)
                .payMethod(orderHelper.getPayMethod(nicePaymentAuth.getPayMethod()))
                .inherenceName(flameName)
                .inherenceNumber(inherenceNumber)
                .build();
    }

    public String createPlainText(OrderReq.RegistrationCard registrationCard) {
        return "cardNo=" + registrationCard.getCardNo() + "&"
                + "expYear=" + registrationCard.getExpYear() + "&"
                + "expMonth=" + registrationCard.getExpMonth() + "&"
                + "idNo=" + registrationCard.getIdNo() + "&"
                + "cardPw=" + registrationCard.getCardPw();
    }

    public NiceBillOkRequest niceBillOk(NicePayBillkeyResponse nicePayBillkeyResponse) {
        return NiceBillOkRequest.builder()
                .cardQuota(0)
                .amount(10)
                .goodsName("카드 확인 용 결제")
                .useShopInterest(false)
                .orderId(nicePayBillkeyResponse.getOrderId())
                .build();
    }

    public RegularPayment RegularPayment(Long id, OrderReq.RegistrationCard registrationCard, NiceBillOkResponse niceBillOkResponse, NicePayBillkeyResponse nicePayBillkeyResponse) {
        return RegularPayment.builder()
                .userId(id)
                .payDate(registrationCard.getPayDate())
                .amount(registrationCard.getAmount())
                .orderId(nicePayBillkeyResponse.getOrderId())
                .bid(nicePayBillkeyResponse.getBid())
                .cardNo(registrationCard.getCardNo())
                .expYear(registrationCard.getExpYear())
                .expMonth(registrationCard.getExpMonth())
                .idNo(registrationCard.getIdNo())
                .cardCode(nicePayBillkeyResponse.getCardCode())
                .cardName(nicePayBillkeyResponse.getCardName())
                .build();
    }
}

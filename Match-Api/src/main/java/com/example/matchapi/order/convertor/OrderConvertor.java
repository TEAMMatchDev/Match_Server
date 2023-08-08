package com.example.matchapi.order.convertor;

import com.example.matchapi.order.dto.OrderReq;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.*;
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
                .regularStatus(RegularStatus.ONE_TIME)
                .build();
    }

    public String createPlainText(OrderReq.RegistrationCard registrationCard) {
        return "cardNo=" + registrationCard.getCardNo() + "&"
                + "expYear=" + registrationCard.getExpYear() + "&"
                + "expMonth=" + registrationCard.getExpMonth() + "&"
                + "idNo=" + registrationCard.getIdNo() + "&"
                + "cardPw=" + registrationCard.getCardPw();
    }

    public NiceBillOkRequest niceBillOk(NicePayBillkeyResponse nicePayBillkeyResponse, String orderId) {
        return NiceBillOkRequest.builder()
                .cardQuota(0)
                .amount(10)
                .goodsName("카드 확인 용 결제")
                .useShopInterest(false)
                .orderId(orderId)
                .build();
    }

    public RegularPayment RegularPayment(Long id, OrderReq.RegistrationCard registrationCard, Long userCardId) {
        return RegularPayment.builder()
                .userId(id)
                .payDate(registrationCard.getPayDate())
                .amount(registrationCard.getAmount())
                .userCardId(userCardId)
                .build();
    }

    public UserCard UserCard(Long id, OrderReq.RegistrationCard registrationCard, NicePayBillkeyResponse nicePayBillkeyResponse){
        return UserCard.builder()
                .userId(id)
                .bid(nicePayBillkeyResponse.getBid())
                .cardNo(registrationCard.getCardNo())
                .expYear(registrationCard.getExpYear())
                .expMonth(registrationCard.getExpMonth())
                .idNo(registrationCard.getIdNo())
                .cardPw(registrationCard.getCardPw())
                .cardCode(nicePayBillkeyResponse.getCardCode())
                .cardName(nicePayBillkeyResponse.getCardName())
                .orderId(nicePayBillkeyResponse.getOrderId())
                .build();
    }

    public RequestPaymentHistory recordHistory(Long userId, String orderId, String tid, int amount, String reason) {
        return RequestPaymentHistory.builder()
                .userId(userId)
                .tid(tid)
                .orderId(orderId)
                .amount(amount)
                .reason(reason)
                .build();
    }
}

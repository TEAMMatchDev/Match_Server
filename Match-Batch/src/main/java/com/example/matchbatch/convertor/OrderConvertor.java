package com.example.matchbatch.convertor;

import com.example.matchbatch.OrderHelper;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.*;
import com.example.matchdomain.donation.repository.RequestPaymentHistoryRepository;
import com.example.matchinfrastructure.pay.nice.dto.NiceBillOkRequest;
import com.example.matchinfrastructure.pay.nice.dto.NiceBillOkResponse;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static java.lang.Integer.parseInt;

@Convertor
@RequiredArgsConstructor
public class OrderConvertor {
    private final OrderHelper orderHelper;
    public NiceBillOkRequest niceBillRequest(RegularPayment regularPayment, String orderId) {
        return NiceBillOkRequest.builder()
                .cardQuota(0)
                .amount(regularPayment.getAmount())
                .goodsName(LocalDateTime.now().getDayOfMonth() + "월 달 " +"MATCH 기부금 정기 결제")
                .useShopInterest(false)
                .orderId(orderId)
                .build();
    }

    public DonationUser donationUser(NiceBillOkResponse niceBillOkResponse, Long userId, String flameName, String inherenceNumber) {
        return DonationUser.builder()
                .userId(userId)
                .price(parseInt(String.valueOf(niceBillOkResponse.getAmount())))
                .tid(niceBillOkResponse.getTid())
                .orderId(niceBillOkResponse.getOrderId())
                .donationStatus(DonationStatus.EXECUTION_BEFORE)
                .payMethod(orderHelper.getPayMethod(niceBillOkResponse.getPayMethod()))
                .inherenceName(flameName)
                .inherenceNumber(inherenceNumber)
                .regularStatus(RegularStatus.REGULAR)
                .build();
    }

    public RequestPaymentHistory RegularHistory(NiceBillOkResponse niceBillOkResponse, Long userId, PaymentStatus paymentStatus, String reason) {
        return RequestPaymentHistory.builder()
                .orderId(niceBillOkResponse.getOrderId())
                .tid(niceBillOkResponse.getTid())
                .userId(userId)
                .paymentStatus(paymentStatus)
                .reason(reason)
                .amount(niceBillOkResponse.getAmount())
                .build();
    }
}
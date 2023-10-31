package com.example.matchbatch.convertor;

import com.example.matchbatch.OrderHelper;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.*;
import com.example.matchdomain.donation.entity.enums.DonationStatus;
import com.example.matchdomain.donation.entity.enums.PayMethod;
import com.example.matchdomain.donation.entity.enums.PaymentStatus;
import com.example.matchdomain.donation.entity.enums.RegularStatus;
import com.example.matchdomain.donation.entity.flameEnum.FlameImage;
import com.example.matchinfrastructure.pay.nice.dto.NiceBillOkRequest;
import com.example.matchinfrastructure.pay.nice.dto.NiceBillOkResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillPayResponse;
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

    public DonationUser donationUser(PortOneBillPayResponse response, Long userId, String flameName, String inherenceNumber, Long projectId, Long regularPaymentId) {
        return DonationUser.builder()
                .userId(userId)
                .price(Long.valueOf(response.getAmount()))
                .tid(response.getImp_uid())
                .orderId(response.getMerchant_uid())
                .donationStatus(DonationStatus.EXECUTION_BEFORE)
                .payMethod(PayMethod.CARD)
                .inherenceName(flameName)
                .inherenceNumber(inherenceNumber)
                .regularStatus(RegularStatus.REGULAR)
                .projectId(projectId)
                .regularPaymentId(regularPaymentId)
                .flameImage(FlameImage.NORMAL_IMG.getImg())
                .build();
    }

    public RequestPaymentHistory RegularHistory(NiceBillOkResponse niceBillOkResponse, Long userId, PaymentStatus paymentStatus, String reason, Long regularPaymentId, int payDate, Long userCardId) {
        return RequestPaymentHistory.builder()
                .orderId(niceBillOkResponse.getOrderId())
                .tid(niceBillOkResponse.getTid())
                .userId(userId)
                .paymentStatus(paymentStatus)
                .reason(reason)
                .amount(niceBillOkResponse.getAmount())
                .regularPaymentId(regularPaymentId)
                .payDate(payDate)
                .userCardId(userCardId)
                .build();
    }
}

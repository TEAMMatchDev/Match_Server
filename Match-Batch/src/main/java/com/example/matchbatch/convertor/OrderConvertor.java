package com.example.matchbatch.convertor;

import com.example.matchbatch.helper.OrderHelper;
import com.example.matchbatch.model.PaymentCntDto;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.*;
import com.example.matchdomain.donation.entity.enums.DonationStatus;
import com.example.matchdomain.donation.entity.enums.PayMethod;
import com.example.matchdomain.donation.entity.enums.RegularStatus;
import com.example.matchdomain.donation.entity.flameEnum.FlameImage;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillPayResponse;
import lombok.RequiredArgsConstructor;

import static java.lang.Integer.parseInt;

@Convertor
@RequiredArgsConstructor
public class OrderConvertor {
    private final OrderHelper orderHelper;

    public DonationUser donationUser(PortOneBillPayResponse response, Long userId, String flameName, String inherenceNumber, Long projectId, Long regularPaymentId) {
        return DonationUser.builder()
                .userId(userId)
                .price((long) response.getAmount())
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

    public PaymentCntDto convertToPaymentCnt(int totalAmount, int successCount) {
        return PaymentCntDto
                .builder()
                .totalAmount(totalAmount)
                .successCnt(successCount)
                .build();
    }

    public RequestFailedHistory convertToRequestFailedHistory(RegularPayment payment, String reason) {
        return RequestFailedHistory
                .builder()
                .regularPaymentId(payment.getId())
                .reason(reason)
                .build();
    }
}

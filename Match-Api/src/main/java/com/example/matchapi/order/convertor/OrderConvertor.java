package com.example.matchapi.order.convertor;

import com.example.matchapi.order.dto.OrderReq;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchapi.portone.dto.PaymentReq;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.*;
import com.example.matchdomain.donation.entity.enums.*;
import com.example.matchdomain.donation.entity.flameEnum.FlameImage;
import com.example.matchdomain.redis.entity.OrderRequest;
import com.example.matchinfrastructure.pay.nice.dto.*;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillPayResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillResponse;
import com.siot.IamportRestClient.response.Payment;
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
                .price(orderDetail.getAmount())
                .tid(nicePaymentAuth.getTid())
                .orderId(nicePaymentAuth.getOrderId())
                .donationStatus(DonationStatus.EXECUTION_BEFORE)
                .payMethod(orderHelper.getPayMethod(nicePaymentAuth.getPayMethod()))
                .inherenceName(flameName)
                .inherenceNumber(inherenceNumber)
                .regularStatus(RegularStatus.ONE_TIME)
                .flameImage(FlameImage.NORMAL_IMG.getImg())
                .build();
    }
    public DonationUser donationUserV2(NicePaymentAuth nicePaymentAuth, Long id, Long amount, String projectId, String flameName, String inherenceNumber) {
        return DonationUser.builder()
                .userId(id)
                .projectId(Long.valueOf(projectId))
                .price(amount)
                .tid(nicePaymentAuth.getTid())
                .orderId(nicePaymentAuth.getOrderId())
                .donationStatus(DonationStatus.EXECUTION_BEFORE)
                .payMethod(orderHelper.getPayMethod(nicePaymentAuth.getPayMethod()))
                .inherenceName(flameName)
                .inherenceNumber(inherenceNumber)
                .regularStatus(RegularStatus.ONE_TIME)
                .flameImage(FlameImage.NORMAL_IMG.getImg())
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
                .amount(10L)
                .goodsName("카드 확인 용 결제")
                .useShopInterest(false)
                .orderId(orderId)
                .build();
    }

    public RegularPayment RegularPayment(Long id, OrderReq.RegularDonation regularDonation, Long userCardId, Long projectId) {
        return RegularPayment.builder()
                .userId(id)
                .payDate(regularDonation.getPayDate())
                .amount(regularDonation.getAmount())
                .userCardId(userCardId)
                .projectId(projectId)
                .regularPayStatus(RegularPayStatus.PROCEEDING)
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
                .cardName(nicePayBillkeyResponse.getCardName())
                .orderId(nicePayBillkeyResponse.getOrderId())
                .build();
    }

    public RequestPaymentHistory recordHistory(Long userId, String orderId, String tid, Long amount, String reason) {
        return RequestPaymentHistory.builder()
                .userId(userId)
                .tid(tid)
                .orderId(orderId)
                .amount(amount)
                .reason(reason)
                .build();
    }

    public OrderRequest CreateRequest(Long userId, Long projectId, String orderId) {
        return OrderRequest.builder()
                .userId(String.valueOf(userId))
                .projectId(String.valueOf(projectId))
                .orderId(orderId)
                .ttl(2000L)
                .build();
    }

    public NiceBillOkRequest billCardOneTime(Long amount, String orderId) {
        return NiceBillOkRequest.builder()
                .cardQuota(0)
                .amount(amount)
                .goodsName("매치 기부금 결제")
                .useShopInterest(false)
                .orderId(orderId)
                .build();
    }

    public DonationUser donationBillUser(NiceBillOkResponse niceBillOkResponse, Long id, Long amount, Long projectId, String flameName, String inherenceNumber, RegularStatus regularStatus, Long regularPaymentId) {
        return DonationUser.builder()
                .userId(id)
                .projectId(projectId)
                .price(amount)
                .tid(niceBillOkResponse.getTid())
                .orderId(niceBillOkResponse.getOrderId())
                .donationStatus(DonationStatus.EXECUTION_BEFORE)
                .payMethod(orderHelper.getPayMethod(niceBillOkResponse.getPayMethod()))
                .inherenceName(flameName)
                .inherenceNumber(inherenceNumber)
                .regularStatus(regularStatus)
                .regularPaymentId(regularPaymentId)
                .build();
    }

    public DonationUser donationUserPortone(Payment payment, Long userId, PaymentReq.ValidatePayment validatePayment, Long projectId, String flameName, String inherenceNumber) {
        return DonationUser.builder()
                .userId(userId)
                .payMethod(orderHelper.getPayMethod(validatePayment.getPayMethod()))
                .projectId(projectId)
                .price((long) validatePayment.getAmount())
                .tid(payment.getImpUid())
                .orderId(payment.getMerchantUid())
                .donationStatus(DonationStatus.EXECUTION_BEFORE)
                .payMethod(orderHelper.getPayMethod(payment.getPayMethod()))
                .inherenceName(flameName)
                .inherenceNumber(inherenceNumber)
                .regularStatus(RegularStatus.ONE_TIME)
                .flameImage(FlameImage.NORMAL_IMG.getImg())
                .build();
    }

    public UserCard UserBillCard(Long id, OrderReq.RegistrationCard registrationCard, PortOneBillResponse portOneBillResponse) {
        return UserCard.builder()
                .userId(id)
                .bid(portOneBillResponse.getCustomer_uid())
                .cardNo(registrationCard.getCardNo())
                .expYear(registrationCard.getExpYear())
                .expMonth(registrationCard.getExpMonth())
                .idNo(registrationCard.getIdNo())
                .cardPw(registrationCard.getCardPw())
                .cardCode(CardCode.getNameByCode(portOneBillResponse.getCard_code()))
                .cardName(portOneBillResponse.getCard_name())
                .customerId(portOneBillResponse.getCustomer_id())
                .cardAbleStatus(CardAbleStatus.ABLE)
                .build();
    }

    public DonationUser donationBillPayUser(PortOneBillPayResponse response, Long id, Long amount, Long projectId, String flameName, String inherenceNumber, RegularStatus regularStatus, Long regularPaymentId) {
        return DonationUser.builder()
                .userId(id)
                .projectId(projectId)
                .price(amount)
                .tid(response.getImp_uid())
                .orderId(response.getMerchant_uid())
                .donationStatus(DonationStatus.EXECUTION_BEFORE)
                .payMethod(PayMethod.CARD)
                .inherenceName(flameName)
                .inherenceNumber(inherenceNumber)
                .regularStatus(regularStatus)
                .regularPaymentId(regularPaymentId)
                .flameImage(FlameImage.NORMAL_IMG.getImg())
                .build();
    }
}

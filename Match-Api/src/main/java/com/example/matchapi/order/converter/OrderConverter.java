package com.example.matchapi.order.converter;

import com.example.matchapi.donation.helper.DonationHelper;
import com.example.matchapi.order.dto.OrderReq;
import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchapi.portone.dto.PaymentReq;
import com.example.matchcommon.annotation.Converter;
import com.example.matchcommon.util.AesUtil;
import com.example.matchdomain.donation.entity.*;
import com.example.matchdomain.donation.entity.enums.*;
import com.example.matchdomain.donation.entity.flameEnum.FlameImage;
import com.example.matchdomain.donation.entity.flameEnum.FlameType;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.redis.entity.OrderRequest;
import com.example.matchinfrastructure.pay.nice.dto.*;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillPayResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

@Converter
@RequiredArgsConstructor
public class OrderConverter {
    private final OrderHelper orderHelper;
    private final DonationHelper donationHelper;
    private final AesUtil aesUtil;

    public RegularPayment convertToRegularPayment(Long id, OrderReq.RegularDonation regularDonation, Long userCardId, Long projectId) {
        return RegularPayment.builder()
                .userId(id)
                .payDate(regularDonation.getPayDate())
                .amount(regularDonation.getAmount())
                .userCardId(userCardId)
                .projectId(projectId)
                .regularPayStatus(RegularPayStatus.PROCEEDING)
                .build();
    }

    public OrderRequest CreateRequest(Long userId, Long projectId, String orderId) {
        return OrderRequest.builder()
                .userId(String.valueOf(userId))
                .projectId(String.valueOf(projectId))
                .orderId(orderId)
                .ttl(2400L)
                .build();
    }
    public DonationUser convertToDonationUserPortone(Long userId, Payment payment, Long projectId, OrderRes.CreateInherenceDto createInherenceDto) {
        return DonationUser.builder()
                .userId(userId)
                .projectId(projectId)
                .price((long) payment.getAmount().intValue())
                .tid(payment.getImpUid())
                .orderId(payment.getMerchantUid())
                .donationStatus(DonationStatus.EXECUTION_BEFORE)
                .payMethod(orderHelper.getPayMethod(payment.getPayMethod()))
                .inherenceName(createInherenceDto.getInherenceName())
                .inherenceNumber(createInherenceDto.getInherenceNumber())
                .regularStatus(RegularStatus.ONE_TIME)
                .flameImage(FlameImage.NORMAL_IMG.getImg())
                .flameType(FlameType.NORMAL_FLAME)
                .build();
    }

    public UserCard convertToUserBillCard(Long id, OrderReq.RegistrationCard registrationCard, PortOneBillResponse portOneBillResponse) {
        return UserCard.builder()
                .userId(id)
                .bid(portOneBillResponse.getCustomer_uid())
                .cardNo(aesUtil.aesCBCEncode(registrationCard.getCardNo()))
                .cardCode(CardCode.getNameByCode(portOneBillResponse.getCard_code()))
                .cardName(portOneBillResponse.getCard_code())
                .customerId(portOneBillResponse.getCustomer_id())
                .cardAbleStatus(CardAbleStatus.ABLE)
                .build();
    }

    public DonationUser donationBillPayUser(PortOneBillPayResponse response, Long id, Long amount, Long projectId, OrderRes.CreateInherenceDto createInherenceDto, RegularStatus regularStatus, Long regularPaymentId) {
        return DonationUser.builder()
                .userId(id)
                .projectId(projectId)
                .price(amount)
                .tid(response.getImp_uid())
                .orderId(response.getMerchant_uid())
                .donationStatus(DonationStatus.EXECUTION_BEFORE)
                .payMethod(PayMethod.CARD)
                .inherenceName(createInherenceDto.getInherenceName())
                .inherenceNumber(createInherenceDto.getInherenceNumber())
                .regularStatus(regularStatus)
                .regularPaymentId(regularPaymentId)
                .flameImage(FlameImage.NORMAL_IMG.getImg())
                .flameType(FlameType.NORMAL_FLAME)
                .build();
    }

    public OrderRes.CompleteDonation convertToCompleteDonation(String name, Project project, Long amount) {
        return OrderRes.CompleteDonation
                .builder()
                .username(name)
                .title(project.getProjectName())
                .usages(project.getUsages())
                .amount(donationHelper.parsePriceComma(Math.toIntExact(amount)))
                .regularStatus(project.getRegularStatus().getName())
                .build();
    }

    public List<OrderRes.UserBillCard> convertToUserCardLists(List<UserCard> userCards) {
        List<OrderRes.UserBillCard> userBillCards = new ArrayList<>();

        userCards.forEach(
                result -> {
                    userBillCards.add(
                            OrderRes.UserBillCard
                                    .builder()
                                    .id(result.getId())
                                    .cardCode(result.getCardCode().getCode())
                                    .cardName(result.getCardCode().getName())
                                    .cardNo(orderHelper.maskMiddleNum(result.getCardNo()))
                                    .cardAbleStatus(result.getCardAbleStatus().getName())
                                    .build()
                            );
                }
        );
        return userBillCards;
    }

    public OrderRequest convertToRequestPrepare(Long userId, Long projectId, int amount, String orderId) {
        return OrderRequest
                .builder()
                .orderId(orderId)
                .userId(String.valueOf(userId))
                .projectId(String.valueOf(projectId))
                .amount(amount)
                .ttl(480L)
                .build();
    }
}

package com.example.matchapi.order.service;

import com.example.matchapi.order.convertor.OrderConvertor;
import com.example.matchapi.order.dto.OrderReq;
import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.order.helper.OrderHelper;
import com.example.matchcommon.exception.InternalServerException;
import com.example.matchcommon.properties.NicePayProperties;
import com.example.matchdomain.donation.entity.UserCard;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.donation.repository.RegularPaymentRepository;
import com.example.matchdomain.donation.repository.RequestPaymentHistoryRepository;
import com.example.matchdomain.donation.repository.UserCardRepository;
import com.example.matchdomain.redis.entity.OrderRequest;
import com.example.matchdomain.redis.repository.OrderRequestRepository;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.repository.UserRepository;
import com.example.matchinfrastructure.pay.nice.client.NiceAuthFeignClient;
import com.example.matchinfrastructure.pay.nice.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.matchdomain.order.exception.RegistrationCardErrorCode.FAILED_ERROR_ENCRYPT;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final NiceAuthFeignClient niceAuthFeignClient;
    private final NicePayProperties nicePayProperties;
    private final DonationUserRepository donationUserRepository;
    private final OrderConvertor orderConvertor;
    private final OrderHelper orderHelper;
    private final RegularPaymentRepository regularPaymentRepository;
    private final UserCardRepository userCardRepository;
    private final RequestPaymentHistoryRepository requestPaymentHistoryRepository;
    private final OrderRequestRepository orderRequestRepository;
    private final UserRepository userRepository;

    public NicePaymentAuth authPayment(String tid, Long amount) {
        String authorizationHeader = orderHelper.getNicePaymentAuthorizationHeader();
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.paymentAuth(authorizationHeader, tid, new NicePayRequest(String.valueOf(amount)));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth.getResultCode(), nicePaymentAuth.getResultMsg());
        return nicePaymentAuth;
    }

    @Transactional
    public String payForProject(OrderReq.OrderDetail orderDetail) {
        String authorizationHeader = orderHelper.getNicePaymentAuthorizationHeader();
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.paymentAuth(authorizationHeader, orderDetail.getTid(), new NicePayRequest(String.valueOf(orderDetail.getAmount())));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth.getResultCode(), nicePaymentAuth.getResultMsg());

        return null;
    }


    @Transactional
    public NicePaymentAuth cancelPayment(String tid, String orderId) {
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.cancelPayment(orderHelper.getNicePaymentAuthorizationHeader(), tid, new NicePayCancelRequest("단순취소", orderId));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth.getResultCode(), nicePaymentAuth.getResultMsg());

        return nicePaymentAuth;
    }

    @Transactional
    public String requestPayment(User user, OrderReq.OrderDetail orderDetail, Long projectId) {
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.
                paymentAuth(orderHelper.getNicePaymentAuthorizationHeader(),
                        orderDetail.getTid(),
                        new NicePayRequest(String.valueOf(orderDetail.getAmount())));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth.getResultCode(), nicePaymentAuth.getResultMsg());

        String flameName = orderHelper.createFlameName(user.getName());

        String inherenceNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "." + createRandomUUID();

        donationUserRepository.save(orderConvertor.donationUser(nicePaymentAuth, user.getId(), orderDetail, projectId, flameName, inherenceNumber));

        return flameName;
    }

    @Transactional
    public void registrationCard(User user, OrderReq.RegistrationCard registrationCard) {
        String encrypt = encrypt(orderConvertor.createPlainText(registrationCard), nicePayProperties.getSecret().substring(0, 32), nicePayProperties.getSecret().substring(0, 16));
        String orderId = createRandomUUID();
        Long userId = user.getId();
        //빌키 발급
        NicePayBillkeyResponse nicePayBillkeyResponse = niceAuthFeignClient.registrationCard(
                orderHelper.getNicePaymentAuthorizationHeader(),
                new NicePayRegistrationCardRequest(encrypt, orderId, "A2"));

        //나이스 카드 확인용 OrderId, Bid 필요
        NiceBillOkResponse niceBillOkResponse = niceAuthFeignClient.billOkRequest(orderHelper.getNicePaymentAuthorizationHeader(), nicePayBillkeyResponse.getBid(), orderConvertor.niceBillOk(nicePayBillkeyResponse, createRandomUUID()));

        //에러코드 핸들링
        orderHelper.checkBillResult(niceBillOkResponse.getResultCode(), niceBillOkResponse.getResultMsg(), niceBillOkResponse.getTid(), niceBillOkResponse.getOrderId());

        //결제 완료 후
        //Billing Key 카드 저장
        UserCard userCard = userCardRepository.save(orderConvertor.UserCard(userId, registrationCard, nicePayBillkeyResponse));

    }


    public String encrypt(String plainText, String secretKey, String iv) {
        SecretKey secureKey = new SecretKeySpec(secretKey.getBytes(), "AES");
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes()));
            byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
            return Hex.encodeHexString(encrypted);
        } catch (Exception e) {
            throw new InternalServerException(FAILED_ERROR_ENCRYPT);
        }
    }


    public String createRandomUUID() {
        return UUID.randomUUID().toString();

    }

    public List<OrderRes.UserBillCard> getUserBillCard(User user) {
        List<UserCard> userCards = user.getUserCard();
        List<OrderRes.UserBillCard> userBillCards = new ArrayList<>();

        userCards.forEach(
                result -> {
                    userBillCards.add(
                            new OrderRes.UserBillCard(
                                    result.getId(),
                                    result.getCardCode(),
                                    result.getCardName(),
                                    orderHelper.maskMiddleNum(result.getCardNo())
                            )
                    );
                }
        );
        return userBillCards;
    }

    @Transactional
    public void deleteBillCard(Long cardId) {
        Optional<UserCard> userCard = userCardRepository.findById(cardId);
        NiceBillExpireResponse niceBillExpireResponse = niceAuthFeignClient.billKeyExpire(orderHelper.getNicePaymentAuthorizationHeader(), userCard.get().getBid(), new NiceBillExpireRequest(createRandomUUID()));
        System.out.println(niceBillExpireResponse.getResultCode() + niceBillExpireResponse.getResultMsg());
        userCardRepository.deleteById(cardId);
    }


    @Transactional
    public void regularDonation(User user, OrderReq.RegularDonation regularDonation, Long cardId) {
        //빌키 저장 후 정기 결제 내역 저장 bid orderId 저장 필요한가? 아니다. orderId 는 결제 시에 항상 새로 만들어줘야함
        System.out.println(cardId);
        regularPaymentRepository.save(orderConvertor.RegularPayment(user.getId(), regularDonation, cardId));
    }

    public String saveRequest(User user, Long projectId, String method) {
        String orderId = createRandomUUID();

        orderRequestRepository.save(orderConvertor.CreateRequest(user.getId(), projectId, orderId,method));

        return orderId;
    }

    public OrderRequest getOrderRequest(String orderId) {
        Optional<OrderRequest> orderRequest = orderRequestRepository.findById(orderId);
        return orderRequest.get();

    }

    public void requestPaymentAuth(String tid, Long amount) {
        NicePaymentAuth nicePaymentAuth = niceAuthFeignClient.
                paymentAuth(orderHelper.getNicePaymentAuthorizationHeader(),
                        tid,
                        new NicePayRequest(String.valueOf(amount)));

        orderHelper.checkNicePaymentsResult(nicePaymentAuth.getResultCode(), nicePaymentAuth.getResultMsg());

        Optional<OrderRequest> orderRequest = orderRequestRepository.findById(nicePaymentAuth.getOrderId());

        Optional<User> user = userRepository.findById(Long.valueOf(orderRequest.get().getUserId()));

        String flameName = orderHelper.createFlameName(user.get().getName());

        String inherenceNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "." + createRandomUUID();

        donationUserRepository.save(orderConvertor.donationUserV2(nicePaymentAuth, user.get().getId(), Math.toIntExact(amount), orderRequest.get().getProjectId(), flameName, inherenceNumber));
    }
}
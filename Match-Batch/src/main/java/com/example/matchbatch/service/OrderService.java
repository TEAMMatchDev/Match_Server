package com.example.matchbatch.service;

import com.example.matchbatch.convertor.OrderConvertor;
import com.example.matchbatch.helper.OrderHelper;
import com.example.matchbatch.helper.TimeUtils;
import com.example.matchbatch.model.CalculateMonthLastDateDto;
import com.example.matchbatch.model.PaymentCntDto;
import com.example.matchcommon.annotation.RedissonLock;
import com.example.matchdomain.donation.adaptor.RegularPaymentAdaptor;
import com.example.matchdomain.donation.adaptor.RequestFailedHistoryAdapter;
import com.example.matchdomain.donation.entity.*;
import com.example.matchinfrastructure.discord.service.DiscordService;
import com.example.matchinfrastructure.pay.portone.client.PortOneFeignClient;
import com.example.matchinfrastructure.pay.portone.convertor.PortOneConvertor;
import com.example.matchinfrastructure.pay.portone.dto.PortOneBillPayResponse;
import com.example.matchinfrastructure.pay.portone.dto.PortOneResponse;
import com.example.matchinfrastructure.pay.portone.service.PortOneAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.matchcommon.constants.MatchStatic.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderHelper orderHelper;
    private final PortOneFeignClient portOneFeignClient;
    private final RegularPaymentAdaptor regularPaymentAdaptor;
    private final RequestFailedHistoryAdapter requestFailedHistoryAdapter;
    private final PortOneAuthService portOneAuthService;
    private final PortOneConvertor portOneConvertor;
    private final DiscordService discordService;
    private final DonationService donationService;
    private final OrderConvertor orderConvertor;
    private final TimeUtils timeUtils;

    public void regularDonationPayment() {
        List<RegularPayment> regularPayments = calculateDay();
        if (regularPayments.isEmpty()) {
            return;
        }

        discordService.sendBatchStartAlert(PAYMENT_ALERT_START, regularPayments.size());

        PaymentCntDto paymentCntDto = paymentsTry(regularPayments, REGULAR_PAYMENT);

        discordService.sendBatchFinishAlert(PAYMENT_ALERT_FINISH, paymentCntDto.getTotalAmount(), regularPayments.size(), paymentCntDto.getSuccessCnt(), regularPayments.size() - paymentCntDto.getSuccessCnt());
    }

    @RedissonLock(LockName = "결제 재시도 LOCK", key = "#jobName")
    public void regularPaymentRetry(String jobName) {
        List<RequestFailedHistory> requestFailedHistories = requestFailedHistoryAdapter.findFailHistory();

        List<RegularPayment> regularPayments = historyToRegularPayments(requestFailedHistories);

        if (regularPayments.isEmpty()) {
            return;
        }

        discordService.sendBatchStartAlert(PAYMENT_RETRY_START, regularPayments.size());

        PaymentCntDto paymentCntDto = paymentsTry(regularPayments, RETRY_PAYMENT);

        discordService.sendBatchFinishAlert(PAYMENT_RETRY_FINISH, paymentCntDto.getTotalAmount(), regularPayments.size(), paymentCntDto.getSuccessCnt(),  regularPayments.size() - paymentCntDto.getSuccessCnt());
    }


    private PaymentCntDto paymentsTry(List<RegularPayment> regularPayments, String type) {
        String accessToken = portOneAuthService.getAuthToken();
        int successCount = 0, totalAmount = 0;

        for (RegularPayment payment : regularPayments) {
            log.info("paymentId: " + payment.getId());
            if (!timeUtils.isPaymentDue(payment, LocalDate.now())) {
                logPaymentSkipped(payment);
                continue;
            }
            boolean isSuccess = processRegularPayment(payment, accessToken, type);

            if(isSuccess) {
                totalAmount += payment.getAmount();
                successCount++;
            }
        }


        return orderConvertor.convertToPaymentCnt(totalAmount, successCount);
    }

    private void logPaymentSkipped(RegularPayment payment) {
        log.info(String.format(PAYMENT_LOG_INFO,
                payment.getUserId(),
                payment.getUserCard().getBid(),
                payment.getAmount(),
                payment.getProjectId(),
                payment.getId()));
    }


    public boolean processRegularPayment(RegularPayment payment, String accessToken, String type) {
        String orderId = orderHelper.createRandomOrderId();

        PortOneResponse<PortOneBillPayResponse> portOneResponse = attemptPayment(payment, accessToken, orderId);
        if(portOneResponse.getCode()!=0){
            handlePaymentFailure(payment, portOneResponse.getMessage(), type);
            return true;
        }
        else {
            handlePaymentSuccess(payment, portOneResponse.getResponse(), type, orderId);
            return false;
        }
    }

    private void handlePaymentFailure(RegularPayment payment, String reason, String type) {
        if (REGULAR_PAYMENT.equals(type)) {
            requestFailedHistoryAdapter.save(orderConvertor.convertToRequestFailedHistory(payment, reason));
        }
        log.error(String.format(FAILED_PAYMENT_LOG, payment.getId(), reason));
    }

    private void handlePaymentSuccess(RegularPayment payment, PortOneBillPayResponse response, String type, String orderId) {
        if (RETRY_PAYMENT.equals(type)) {
            requestFailedHistoryAdapter.deleteByRegularPaymentId(payment.getId());
        }
        donationService.processSaveDonationPayment(response, payment);
        log.info(String.format(SUCCESS_PAYMENT_LOG,
                payment.getUserId(),
                orderId,
                payment.getUserCard().getBid(),
                payment.getAmount(),
                payment.getProjectId()));
    }


    private PortOneResponse<PortOneBillPayResponse> attemptPayment(RegularPayment payment, String accessToken, String orderId) {
        UserCard userCard = payment.getUserCard();
        return portOneFeignClient.payWithBillKey(
                accessToken,
                portOneConvertor.convertPayWithBillKey(
                        userCard.getBid(),
                        orderId,
                        payment.getAmount(),
                        PAY_TITLE,
                        userCard.getCustomerId()));
    }

    public List<RegularPayment> calculateDay() {
        CalculateMonthLastDateDto date = timeUtils.calculateMonthLastDate();

        if (date.getCurrentDay() == date.getLastDayOfMonth()) {
            return regularPaymentAdaptor.findByLastDayRegularPayment(date.getCurrentDay());
        } else {
            return regularPaymentAdaptor.findByDate(date.getCurrentDay());
        }
    }

    private List<RegularPayment> historyToRegularPayments(List<RequestFailedHistory> requestFailedHistories) {
        return requestFailedHistories.stream()
                .map(RequestFailedHistory::getRegularPayment).collect(Collectors.toList());
    }
}

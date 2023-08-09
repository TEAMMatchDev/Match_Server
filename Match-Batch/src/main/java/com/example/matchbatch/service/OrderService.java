package com.example.matchbatch.service;

import com.example.matchbatch.OrderHelper;
import com.example.matchbatch.convertor.OrderConvertor;
import com.example.matchcommon.properties.NicePayProperties;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.donation.entity.RequestPaymentHistory;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.donation.repository.RegularPaymentRepository;
import com.example.matchdomain.donation.repository.RequestPaymentHistoryRepository;
import com.example.matchinfrastructure.pay.nice.client.NiceAuthFeignClient;
import com.example.matchinfrastructure.pay.nice.dto.NiceBillOkResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.matchdomain.donation.entity.PaymentStatus.COMPLETE;
import static com.example.matchdomain.donation.entity.PaymentStatus.FAIL;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final NiceAuthFeignClient niceAuthFeignClient;
    private final RegularPaymentRepository regularPaymentRepository;
    private final OrderHelper orderHelper;
    private final OrderConvertor orderConvertor;
    private final RequestPaymentHistoryRepository regularPaymentHistoryRepository;
    private final DonationUserRepository donationUserRepository;

    public void regularDonationPayment() {

        List<RegularPayment> regularPayments = calculateDay();
        List<RequestPaymentHistory> requestPaymentHistories = new ArrayList<>();
        List<DonationUser> donationUsers = new ArrayList<>();

        if (regularPayments.size() > 0) {
            for (RegularPayment regularPayment : regularPayments) {
                Long userId = regularPayment.getUserId();
                NiceBillOkResponse niceBillOkResponse = niceAuthFeignClient.billOkRequest(orderHelper.getNicePaymentAuthorizationHeader(),
                        regularPayment.getUserCard().getBid(),
                        orderConvertor.niceBillRequest(regularPayment, createRandomUUID()));
                if (niceBillOkResponse.getResultCode().equals("0000")){
                    String flameName = orderHelper.createFlameName(regularPayment.getUser().getName());

                    String inherenceNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "." + createRandomUUID();

                    donationUsers.add(orderConvertor.donationUser(niceBillOkResponse, userId, flameName, inherenceNumber));

                    requestPaymentHistories.add(orderConvertor.RegularHistory(niceBillOkResponse, userId , COMPLETE, "SUCCESS"));
                    log.info("success Payment "+ "userId :"+ regularPayment.getUserId() + " orderId : " + niceBillOkResponse.getOrderId() + " bid :" + regularPayment.getUserCard().getBid() + " amount :" + regularPayment.getAmount());
                }
                else{
                    requestPaymentHistories.add(orderConvertor.RegularHistory(niceBillOkResponse, userId , FAIL, niceBillOkResponse.getResultMsg()));

                    log.info("fail Payment "+ "userId :"+ regularPayment.getUserId() + " orderId : " + niceBillOkResponse.getOrderId() + " bid :" + regularPayment.getUserCard().getBid() + " amount :" + regularPayment.getAmount());
                }
            }
            donationUserRepository.saveAll(donationUsers);
            regularPaymentHistoryRepository.saveAll(requestPaymentHistories);
        }

    }

    private List<RegularPayment> calculateDay() {
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // 이번달에 마지막 날짜
        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 오늘 날짜 구하기
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (currentDay == lastDayOfMonth) {
            //현재 날짜와 달의 마지막 날짜가 같거나 클때의 로직
            System.out.println("현재 날짜가 같아요");
            return regularPaymentRepository.findByPayDateGreaterThanEqual(currentDay);
        } else {
            // 현재 날짜가 같지 않을 때의 로직
            System.out.println("현재 날짜가 달라요");
            return regularPaymentRepository.findByPayDate(currentDay);
        }
    }

    public String createRandomUUID() {
        return UUID.randomUUID().toString();

    }
}

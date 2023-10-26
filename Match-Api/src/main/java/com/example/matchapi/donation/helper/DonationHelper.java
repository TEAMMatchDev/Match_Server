package com.example.matchapi.donation.helper;

import com.example.matchcommon.annotation.Helper;
import com.example.matchdomain.common.model.MessageType;
import com.example.matchdomain.common.model.RandomMessage;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.enums.DonationStatus;
import lombok.RequiredArgsConstructor;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.example.matchdomain.common.model.MessageType.*;
import static com.example.matchdomain.donation.entity.enums.RegularStatus.ONE_TIME;

@Helper
@RequiredArgsConstructor
public class DonationHelper {
    public String parsePriceComma(int amount){
        DecimalFormat decimalFormat = new DecimalFormat("#,###"); // 포맷을 설정합니다.

        return decimalFormat.format(amount)+"원";
    }
    public String createRandomMessage(DonationUser donationUser) {
        switch(donationUser.getDonationStatus()){
            case EXECUTION_BEFORE:
                if(!isOneDayPassed(donationUser.getCreatedAt())){
                    return getRandomMessageType(PAY_SUCCESS);
                }
                else{
                    return getRandomMessageType(PAY_ONE_DAY);
                }
            case EXECUTION_UNDER:
                return getRandomMessageType(UNDER);
            case EXECUTION_SUCCESS:
                return getRandomMessageType(COMPLETE);
        }
        return "하이";
    }

    public int getDonationSequence(DonationUser donationUser, Long donationId) {

        if(donationUser.getRegularStatus() == ONE_TIME){
            return 0;
        }
        else{
            return donationIdLists(donationUser.getRegularPayment().getDonationUser()).indexOf(donationId) + 1;
        }
    }

    public List<Long> donationIdLists(List<DonationUser> donationUser) {
        return donationUser.stream().map(DonationUser::getId).collect(Collectors.toList());
    }


    public String getRandomMessageType(MessageType messageType) {
        List<RandomMessage> paySuccessMessages = Arrays.stream(RandomMessage.values())
                .filter(rm -> rm.getMessageType() == messageType)
                .collect(Collectors.toList());

        Random random = new Random();
        return paySuccessMessages.get(random.nextInt(paySuccessMessages.size())).getMessage();
    }

    public boolean isOneDayPassed(LocalDateTime dateTime) {
        return Duration.between(dateTime, LocalDateTime.now()).toDays() >= 1;
    }
}

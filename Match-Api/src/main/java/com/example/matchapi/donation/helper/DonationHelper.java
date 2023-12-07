package com.example.matchapi.donation.helper;

import com.example.matchcommon.annotation.Helper;
import com.example.matchdomain.common.model.MessageType;
import com.example.matchdomain.common.model.RandomMessage;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.enums.DonationStatus;
import com.example.matchdomain.project.entity.enums.ProjectKind;
import lombok.RequiredArgsConstructor;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.example.matchcommon.constants.RandomMessageStatic.*;
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
            case PARTIAL_EXECUTION:
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

    public String createRandomMessageTutorial(ProjectKind projectKind) {
        switch (projectKind){
            case CHILDREN:
                return CHILDREN_MESSAGE;
            case YOUTH:
                return YOUTH_MESSAGE;
            case WOMEN:
                return WOMEN_MESSAGE;
            case ELDER:
                return ELDER_MESSAGE;
            case DISABLED:
                return DISABLED_MESSAGE;
            case SOCIAL:
                return SOCIAL_MESSAGE;
            case NEIGHBOR:
                return NEIGHBOR_MESSAGE;
            case EARTH:
                return EARTH_MESSAGE;
            case ANIMAL:
                return ANIMAL_MESSAGE;
            case ENVIRONMENT:
                return ENVIRONMENT_MESSAGE;
            default:
                return RANDOM_MESSAGE;
        }
    }
}

package com.example.matchapi.order.helper;

import com.example.matchapi.order.dto.OrderRes;
import com.example.matchcommon.annotation.Helper;
import com.example.matchcommon.util.AesUtil;
import com.example.matchdomain.donation.adaptor.DonationAdaptor;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.enums.PayMethod;
import com.example.matchdomain.donation.entity.flameEnum.Adjective;
import com.example.matchdomain.donation.entity.flameEnum.AdjectiveFlame;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.matchcommon.constants.MatchStatic.BASIC;

@Helper
@RequiredArgsConstructor
public class OrderHelper {
    private final DonationAdaptor donationAdaptor;
    private final AesUtil aesUtil;

    public PayMethod getPayMethod(String value) {
        for (PayMethod payMethod : PayMethod.values()) {
            if(payMethod.getValue().equalsIgnoreCase(value)){
                return payMethod;
            }
        }
        return null;
    }

    public List<String> getInherenceName(List<DonationUser> donationUsers){
        return donationUsers.stream()
                .map(DonationUser :: getInherenceName).collect(Collectors.toList());
    }

    public String createFlameName(User user) {
        List<DonationUser> donationUsers = donationAdaptor.findDonationListsByUser(user);

        List<String> inherenceNames = getInherenceName(donationUsers);

        String randomName;
        do {
            randomName = user.getName() + "님의 " + getRandomEnumValue(AdjectiveFlame.class).getValue() + " " + getRandomEnumValue(Adjective.class).getValue() +  " 불꽃이";
        } while (inherenceNames.contains(randomName));

        return randomName;
    }


    public static <T extends Enum<?>> T getRandomEnumValue(Class<T> enumClass) {
        Random random = new Random();
        T[] values = enumClass.getEnumConstants();
        return values[random.nextInt(values.length)];
    }

    public String maskMiddleNum(String cardNo) {
        cardNo = aesUtil.aesCBCDecode(cardNo);
        String firstFourDigits = cardNo.substring(0, 4);
        String lastFourDigits = cardNo.substring(12);
        String middleDigitsMasked = "********";

        return firstFourDigits + middleDigitsMasked + lastFourDigits;
    }

    public String createOrderId(String type){
        boolean useLetters = true;
        boolean useNumbers = true;
        String randomStr = RandomStringUtils.random(12, useLetters, useNumbers);
        return type + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "-" + randomStr;
    }

    public OrderRes.CreateInherenceDto createInherence(User user) {

        String flameName = createFlameName(user);

        String inherenceNumber = createRandomUUID();

        return new OrderRes.CreateInherenceDto(flameName, inherenceNumber);
    }

    public String createRandomUUID() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "." + UUID.randomUUID();
    }

    public String formatString(String input, int length) {
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            if (i > 0 && i % length == 0) {
                formatted.append('-');
            }
            formatted.append(input.charAt(i));
        }

        return formatted.toString();
    }
}

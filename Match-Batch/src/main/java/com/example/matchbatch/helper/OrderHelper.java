package com.example.matchbatch.helper;

import com.example.matchcommon.annotation.Helper;
import com.example.matchcommon.properties.NicePayProperties;
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
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.example.matchcommon.constants.MatchStatic.BASIC;
import static com.example.matchcommon.constants.MatchStatic.REGULAR;

@RequiredArgsConstructor
@Helper
public class OrderHelper {

    private final NicePayProperties nicePayProperties;
    private final DonationAdaptor donationAdaptor;
    public String getNicePaymentAuthorizationHeader() {
        return BASIC + Base64.getEncoder().encodeToString((nicePayProperties.getClient() + ":" + nicePayProperties.getSecret()).getBytes());
    }

    public String createFlameName(User user) {
        List<DonationUser> donationUsers = donationAdaptor.findDonationListsByUser(user);

        List<String> inherenceNames = getInherenceName(donationUsers);

        String randomName;
        do {
            randomName = getRandomEnumValue(AdjectiveFlame.class).getValue() +  " 불꽃이";
        } while (inherenceNames.contains(randomName));

        return randomName;
    }

    public List<String> getInherenceName(List<DonationUser> donationUsers){
        return donationUsers.stream()
                .map(DonationUser :: getInherenceName).collect(Collectors.toList());
    }


    public static <T extends Enum<?>> T getRandomEnumValue(Class<T> enumClass) {
        Random random = new Random();
        T[] values = enumClass.getEnumConstants();
        return values[random.nextInt(values.length)];
    }

    public PayMethod getPayMethod(String value) {
        for (PayMethod payMethod : PayMethod.values()) {
            if(payMethod.getValue().equalsIgnoreCase(value)){
                return payMethod;
            }
        }
        return null;
    }

    public String createRandomOrderId() {
        boolean useLetters = true;
        boolean useNumbers = true;
        String randomStr = RandomStringUtils.random(12, useLetters, useNumbers);

        return REGULAR + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.HH:mm")) + "-" + randomStr;
    }
}

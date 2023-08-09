package com.example.matchbatch;

import com.example.matchcommon.annotation.Helper;
import com.example.matchcommon.properties.NicePayProperties;
import com.example.matchdomain.donation.entity.PayMethod;
import com.example.matchdomain.donation.entity.flameEnum.Adjective;
import com.example.matchdomain.donation.entity.flameEnum.AdjectiveFlame;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;
import java.util.Random;

import static com.example.matchcommon.constants.MatchStatic.BASIC;

@RequiredArgsConstructor
@Helper
public class OrderHelper {

    private final NicePayProperties nicePayProperties;
    private final DonationUserRepository donationUserRepository;
    public String getNicePaymentAuthorizationHeader() {
        return BASIC + Base64.getEncoder().encodeToString((nicePayProperties.getClient() + ":" + nicePayProperties.getSecret()).getBytes());
    }

    public String createFlameName(String name) {
        String randomName;
        do {
            randomName = name + "님의 " + getRandomEnumValue(AdjectiveFlame.class).getValue() + " " + getRandomEnumValue(Adjective.class).getValue() +  " 불꽃이";
        } while (donationUserRepository.existsByInherenceName(randomName));

        return randomName;
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
}

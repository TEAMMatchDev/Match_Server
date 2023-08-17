package com.example.matchapi.donation.convertor;

import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.DonationUser;
import org.springframework.format.annotation.DateTimeFormat;

@Convertor
public class DonationConvertor {
    public DonationRes.FlameList Flame(DonationUser result) {
        return DonationRes.FlameList.builder()
                .donationId(result.getId())
                .amount(result.getPrice())
                .donationStatus(result.getDonationStatus().getName())
                .flameName(result.getInherenceName())
                .createdAt(result.getCreatedAt().getYear()+"년 "
                        +result.getCreatedAt().getMonthValue()+"월 "
                        +result.getCreatedAt().getDayOfMonth()+"일 "
                        +result.getCreatedAt().getHour()+"시 "
                        +result.getCreatedAt().getMinute()+"분")
                .build();
    }
}

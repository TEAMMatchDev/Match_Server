package com.example.matchapi.donation.convertor;

import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.helper.DonationHelper;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.DonationUser;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

import static com.example.matchdomain.donation.entity.DonationStatus.*;

@Convertor
@RequiredArgsConstructor
public class DonationConvertor {
    private final DonationHelper donationHelper;
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

    public DonationRes.DonationCount DonationCount(List<DonationUser> donationUserList) { int beforeCnt=0;
        int underCnt=0;
        int successCnt=0;

        for (DonationUser donationUser : donationUserList) {
            if (donationUser.getDonationStatus() == EXECUTION_BEFORE) {
                beforeCnt += 1;
            } else if (donationUser.getDonationStatus() == EXECUTION_UNDER) {
                underCnt += 1;
            } else if (donationUser.getDonationStatus() == EXECUTION_SUCCESS) {
                successCnt += 1;
            }
        }
        return DonationRes.DonationCount.builder()
                .beforeCnt(beforeCnt)
                .underCnt(underCnt)
                .successCnt(successCnt)
                .build();


    }

    public DonationRes.DonationList DonationList(DonationUser result) {
        String payDate="";
        if(result.getRegularPayment()!=null) {
            payDate = "매월 " + result.getRegularPayment().getPayDate() + "일 " + donationHelper.parsePriceComma(Math.toIntExact(result.getRegularPayment().getAmount()));
        }else{
            payDate = "단기 후원 " + result.getPrice() + "원";
        }

        return DonationRes.DonationList.builder()
                .donationId(result.getId())
                .donationDate(String.valueOf(result.getCreatedAt().getYear()).replace("20","")+"."+result.getCreatedAt().getMonthValue())
                .donationStatus(result.getDonationStatus().getName())
                .projectName(result.getProject().getProjectName())
                .regularStatus(result.getRegularStatus().getName())
                .regularDate(payDate)
                .build();
    }
}

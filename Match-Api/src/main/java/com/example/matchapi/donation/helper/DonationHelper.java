package com.example.matchapi.donation.helper;

import com.example.matchcommon.annotation.Helper;
import com.example.matchdomain.donation.entity.DonationUser;
import lombok.RequiredArgsConstructor;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.matchdomain.donation.entity.enums.RegularStatus.ONE_TIME;

@Helper
@RequiredArgsConstructor
public class DonationHelper {
    public String parsePriceComma(int amount){
        DecimalFormat decimalFormat = new DecimalFormat("#,###"); // 포맷을 설정합니다.

        return decimalFormat.format(amount)+"원";
    }

    public String createRandomMessage() {
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
}

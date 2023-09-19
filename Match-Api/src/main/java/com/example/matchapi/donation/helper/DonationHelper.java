package com.example.matchapi.donation.helper;

import com.example.matchcommon.annotation.Helper;
import lombok.RequiredArgsConstructor;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Helper
@RequiredArgsConstructor
public class DonationHelper {
    public String parsePriceComma(int amount){
        DecimalFormat decimalFormat = new DecimalFormat("#,###"); // 포맷을 설정합니다.

        return decimalFormat.format(amount)+"원";
    }

    public String timeFormat(LocalDateTime createdAt) {
        return createdAt.getYear() + "." + createdAt.getMonthValue() + "." + createdAt.getDayOfMonth();
    }
}

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

    public String dayTimeFormat(LocalDateTime createdAt) {
        return createdAt.getYear() + "." + createdAt.getMonthValue() + "." + createdAt.getDayOfMonth() + " " + checkTimes(createdAt.getHour()) + ":" + checkTimes(createdAt.getMinute());
    }

    public String checkTimes(int time){
        if(time < 10){
            return  "0" + time;
        }
        else{
            return String.valueOf(time);
        }
    }
}

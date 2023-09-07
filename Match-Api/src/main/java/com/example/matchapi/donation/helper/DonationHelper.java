package com.example.matchapi.donation.helper;

import com.example.matchcommon.annotation.Helper;
import lombok.RequiredArgsConstructor;

import java.text.DecimalFormat;

@Helper
@RequiredArgsConstructor
public class DonationHelper {
    public String parsePriceComma(int amount){
        DecimalFormat decimalFormat = new DecimalFormat("#,###"); // 포맷을 설정합니다.

        return decimalFormat.format(amount)+"원";
    }
}

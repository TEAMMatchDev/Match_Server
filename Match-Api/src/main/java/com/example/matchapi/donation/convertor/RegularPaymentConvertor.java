package com.example.matchapi.donation.convertor;

import com.example.matchapi.common.util.TimeHelper;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.helper.DonationHelper;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.RegularPayment;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Convertor
@RequiredArgsConstructor
public class RegularPaymentConvertor {
    private final TimeHelper timeHelper;
    private final DonationHelper donationHelper;
    public List<DonationRes.MatchList> MatchList(List<RegularPayment> regularPayments) {
        List<DonationRes.MatchList> matchLists = new ArrayList<>();

        regularPayments.forEach(
                result -> matchLists.add(MatchListDetail(result))
        );

        return matchLists;
    }

    private DonationRes.MatchList MatchListDetail(RegularPayment result) {
        return DonationRes.MatchList
                .builder()
                .regularDate(timeHelper.matchTimeFormat(result.getCreatedAt()))
                .projectTitle(result.getProject().getProjectName())
                .regularPayStatus(result.getRegularPayStatus())
                .payDate(result.getPayDate())
                .amount(donationHelper.parsePriceComma(Math.toIntExact(result.getAmount())))
                .build();
    }
}

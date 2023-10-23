package com.example.matchapi.donation.convertor;

import com.example.matchapi.common.util.TimeHelper;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.helper.DonationHelper;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.DonationUser;
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

    public List<DonationRes.BurningFlameDto> BurningFlameList(List<DonationUser> donationUsers) {
        List<DonationRes.BurningFlameDto> burningFlameDto = new ArrayList<>();

        donationUsers.forEach(
                result -> burningFlameDto.add(
                        BurningFlame(result)
                )
        );

        return burningFlameDto;

    }

    private DonationRes.BurningFlameDto BurningFlame(DonationUser donationUser) {

        return DonationRes.BurningFlameDto
                .builder()
                .donationId(donationUser.getId())
                .usages(donationUser.getProject().getUsages())
                .image(donationUser.getFlameImage())
                .inherenceName(donationUser.getInherenceName())
                .randomMessage(donationHelper.createRandomMessage())
                .build();
    }
}

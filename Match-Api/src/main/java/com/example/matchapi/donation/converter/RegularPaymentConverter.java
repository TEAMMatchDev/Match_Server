package com.example.matchapi.donation.converter;

import com.example.matchapi.common.util.TimeHelper;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.helper.DonationHelper;
import com.example.matchcommon.annotation.Converter;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.RegularPayment;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Converter
@RequiredArgsConstructor
public class RegularPaymentConverter {
    private final TimeHelper timeHelper;
    private final DonationHelper donationHelper;
    public List<DonationRes.MatchList> convertToMatchList(List<RegularPayment> regularPayments) {
        List<DonationRes.MatchList> matchLists = new ArrayList<>();

        regularPayments.forEach(
                result -> matchLists.add(convertToMatchListDetail(result))
        );

        return matchLists;
    }

    private DonationRes.MatchList convertToMatchListDetail(RegularPayment result) {
        return DonationRes.MatchList
                .builder()
                .regularId(result.getId())
                .regularDate(timeHelper.matchTimeFormat(result.getCreatedAt()))
                .projectTitle(result.getProject().getProjectName())
                .regularPayStatus(result.getRegularPayStatus())
                .payDate(result.getPayDate())
                .amount(donationHelper.parsePriceComma(Math.toIntExact(result.getAmount())))
                .build();
    }

    public List<DonationRes.BurningFlameDto> convertToBurningFlameList(List<DonationUser> donationUsers) {
        List<DonationRes.BurningFlameDto> burningFlameDto = new ArrayList<>();

        donationUsers.forEach(
                result -> burningFlameDto.add(
                        convertToBurningFlame(result)
                )
        );

        return burningFlameDto;

    }

    private DonationRes.BurningFlameDto convertToBurningFlame(DonationUser donationUser) {

        return DonationRes.BurningFlameDto
                .builder()
                .donationId(donationUser.getId())
                .projectId(donationUser.getProjectId())
                .usages(donationUser.getProject().getUsages())
                .image(donationUser.getFlameImage())
                .inherenceName(donationUser.getInherenceName())
                .randomMessage(donationHelper.createRandomMessage(donationUser))
                .build();
    }
}

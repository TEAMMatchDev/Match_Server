package com.example.matchbatch.converter;

import com.example.matchcommon.annotation.Converter;
import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.enums.HistoryStatus;

import static com.example.matchdomain.donation.entity.enums.HistoryStatus.CREATE;

@Converter
public class DonationConverter {
    public DonationHistory DonationHistory(Long id, HistoryStatus historyStatus) {
        return DonationHistory.builder()
                .donationUserId(id)
                .historyStatus(historyStatus)
                .build();
    }

    public DonationHistory convertToDonationHistory(DonationUser donationUser) {
        return DonationHistory.builder()
                .donationUserId(donationUser.getId())
                .regularPaymentId(donationUser.getRegularPaymentId())
                .historyStatus(CREATE)
                .build();
    }
}

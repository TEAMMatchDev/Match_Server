package com.example.matchbatch.convertor;

import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.enums.HistoryStatus;

import static com.example.matchdomain.donation.entity.enums.HistoryStatus.CREATE;

@Convertor
public class DonationConvertor {
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

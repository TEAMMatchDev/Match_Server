package com.example.matchbatch.convertor;

import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.enums.HistoryStatus;

@Convertor
public class DonationConvertor {
    public DonationHistory DonationHistory(Long id, HistoryStatus historyStatus) {
        return DonationHistory.builder()
                .donationUserId(id)
                .historyStatus(historyStatus)
                .build();
    }
}

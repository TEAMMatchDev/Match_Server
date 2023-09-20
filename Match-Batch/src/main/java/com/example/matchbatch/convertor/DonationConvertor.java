package com.example.matchbatch.convertor;

import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.HistoryStatus;

@Convertor
public class DonationConvertor {
    public DonationHistory DonationHistory(Long id, HistoryStatus historyStatus, Long regularPaymentId) {
        return DonationHistory.builder()
                .donationUserId(id)
                .historyStatus(historyStatus)
                .regularPaymentId(regularPaymentId)
                .build();
    }
}
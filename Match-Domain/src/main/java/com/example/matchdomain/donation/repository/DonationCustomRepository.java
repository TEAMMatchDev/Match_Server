package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.enums.HistoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DonationCustomRepository {
    Page<DonationHistory> getDonationHistoryCustom(Long regularPaymentId, Long donationId, HistoryStatus historyStatus, Pageable pageable, Long projectId);
}

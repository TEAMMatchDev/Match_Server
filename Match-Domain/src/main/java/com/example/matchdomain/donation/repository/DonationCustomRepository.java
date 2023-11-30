package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.enums.HistoryStatus;
import com.example.matchdomain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DonationCustomRepository {
    Page<DonationHistory> getDonationHistoryCustom(Long regularPaymentId, Long donationId, HistoryStatus historyStatus, Pageable pageable, Long projectId);

    List<DonationUser> checkPopUp(User user);
}

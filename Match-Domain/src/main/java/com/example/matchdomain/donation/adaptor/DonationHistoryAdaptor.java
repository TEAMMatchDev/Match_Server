package com.example.matchdomain.donation.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.enums.HistoryStatus;
import com.example.matchdomain.donation.repository.DonationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.example.matchdomain.donation.entity.enums.HistoryStatus.CREATE;

@Adaptor
@RequiredArgsConstructor
public class DonationHistoryAdaptor {
    private final DonationHistoryRepository donationHistoryRepository;

    public Page<DonationHistory> findDonationRegularList(Long regularPayId, Long projectId, HistoryStatus turnOn, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return donationHistoryRepository.findByDonationUser_RegularPaymentIdOrRegularPaymentIdOrProjectIdAndHistoryStatusNotOrderByCreatedAtAsc(regularPayId, regularPayId, projectId, HistoryStatus.TURN_ON ,pageable);
    }

    public Page<DonationHistory> findMatchHistory(Long projectId, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return donationHistoryRepository.findByDonationUser_ProjectOrProjectIdIdOrderByCreatedAtDesc(projectId, pageable);
    }

    public Page<DonationHistory> findDonationHistory(DonationUser donationUser, Long donationId, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return donationHistoryRepository.getDonationHistoryCustom(donationUser.getRegularPaymentId(), donationId, CREATE, pageable, donationUser.getProjectId());
    }

    public void saveDonationHistory(DonationHistory donationHistory) {
        donationHistoryRepository.save(donationHistory);
    }
}

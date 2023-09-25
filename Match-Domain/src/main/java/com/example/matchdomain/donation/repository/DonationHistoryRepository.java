package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.HistoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonationHistoryRepository extends JpaRepository<DonationHistory, Long>, DonationCustomRepository {
    @Query(value = "SELECT DH FROM DonationHistory DH " +
            "LEFT JOIN FETCH DH.donationUser DU LEFT JOIN FETCH DU.user " +
            "WHERE DH.regularPaymentId = :regularPayId " +
            "ORDER BY DH.createdAt DESC",
            countQuery = "SELECT COUNT(DH) FROM DonationHistory DH where DH.regularPaymentId =: regularPayId ")
    Page<DonationHistory> findByRegularPaymentIdOrderByCreatedAtDesc(@Param("regularPayId") Long regularPayId, Pageable pageable);


}

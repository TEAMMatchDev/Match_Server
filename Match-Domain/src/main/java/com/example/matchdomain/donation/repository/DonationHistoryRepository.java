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
            "WHERE DH.regularPaymentId = :regularPayId and DH.historyStatus != :historyStatus " +
            "ORDER BY DH.createdAt ASC",
            countQuery = "SELECT COUNT(DH) FROM DonationHistory DH where DH.regularPaymentId =: regularPayId and DH.historyStatus != :historyStatus")
    Page<DonationHistory> findByRegularPaymentIdAndHistoryStatusNotOrderByCreatedAtAsc(@Param("regularPayId") Long regularPayId,@Param("historyStatus") HistoryStatus historyStatus, Pageable pageable);


    @Query(value = "SELECT DH FROM DonationHistory DH " +
            "LEFT JOIN FETCH DH.donationUser DU " +
            "LEFT JOIN FETCH DU.user U " +
            "where DU.projectId = :projectId or DH.projectId = :projectId ORDER BY DH.createdAt ASC",
            countQuery = "SELECT COUNT(DH) FROM DonationHistory DH " +
                    "LEFT JOIN DH.donationUser DU " +
                    "LEFT JOIN DU.user U " +
                    "where DU.projectId = :projectId or DH.projectId = :projectId")
    Page<DonationHistory> findByDonationUser_ProjectOrProjectIdIdOrderByCreatedAtAsc(@Param("projectId") Long projectId, Pageable pageable);
}

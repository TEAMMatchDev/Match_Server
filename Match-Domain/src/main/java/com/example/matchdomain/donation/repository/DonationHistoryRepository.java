package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.enums.HistoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DonationHistoryRepository extends JpaRepository<DonationHistory, Long>, DonationCustomRepository {
    @Query(value = "SELECT DH FROM DonationHistory DH " +
            "LEFT JOIN FETCH DH.donationUser DU " +
            "LEFT JOIN FETCH DU.user " +
            "WHERE DU.regularPaymentId = :regularPayId or DU.regularPaymentId = :regularPayId2 or DH.projectId = :projectId and DH.historyStatus != :historyStatus " +
            "ORDER BY DH.createdAt ASC",
            countQuery = "SELECT COUNT(DH) FROM DonationHistory DH LEFT JOIN DH.donationUser DU " +
                    "LEFT JOIN DU.user " +
                    "WHERE DU.regularPaymentId = :regularPayId " +
                    "or DU.regularPaymentId = :regularPayId2 " +
                    "or DH.projectId = :projectId " +
                    "and DH.historyStatus != :historyStatus "
    )
    Page<DonationHistory> findByDonationUser_RegularPaymentIdOrRegularPaymentIdOrProjectIdAndHistoryStatusNotOrderByCreatedAtAsc(@Param("regularPayId") Long regularPayId,@Param("regularPayId2") Long regularPayId2,@Param("projectId") Long projectId, @Param("historyStatus") HistoryStatus historyStatus, Pageable pageable);


    @Query(value = "SELECT DH FROM DonationHistory DH " +
            "LEFT JOIN FETCH DH.donationUser DU " +
            "LEFT JOIN FETCH DU.user U " +
            "where DU.projectId = :projectId or DH.projectId = :projectId ORDER BY DH.createdAt DESC",
            countQuery = "SELECT COUNT(DH) FROM DonationHistory DH " +
                    "LEFT JOIN DH.donationUser DU " +
                    "LEFT JOIN DU.user U " +
                    "where DU.projectId = :projectId or DH.projectId = :projectId")
    Page<DonationHistory> findByDonationUser_ProjectOrProjectIdIdOrderByCreatedAtDesc(@Param("projectId") Long projectId, Pageable pageable);
}

package com.example.matchdomain.donation.repository;

import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.RequestFailedHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestFailedHistoryRepository extends JpaRepository<RequestFailedHistory,Long> {

    @Query("SELECT DISTINCT rf FROM RequestFailedHistory rf WHERE rf.status = :status GROUP BY rf.regularPaymentId")
    List<RequestFailedHistory> findDistinctByStatus(@Param("status") Status status);

    void deleteByRegularPaymentId(Long regularPaymentId);
}

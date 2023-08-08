package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.RequestPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestPaymentHistoryRepository extends JpaRepository<RequestPaymentHistory,Long> {
}

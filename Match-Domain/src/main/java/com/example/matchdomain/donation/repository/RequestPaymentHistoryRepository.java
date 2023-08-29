package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.PaymentStatus;
import com.example.matchdomain.donation.entity.RequestPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestPaymentHistoryRepository extends JpaRepository<RequestPaymentHistory,Long> {
    List<RequestPaymentHistory> findByPayDateGreaterThanEqualAndPaymentStatus(int currentDay, PaymentStatus paymentStatus);

    List<RequestPaymentHistory> findByPayDateAndPaymentStatus(int currentDay, PaymentStatus paymentStatus);
}

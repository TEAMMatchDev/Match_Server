package com.example.matchdomain.donation.repository;


import com.example.matchdomain.donation.entity.RegularPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RegularPaymentHistoryRepository extends JpaRepository<RegularPaymentHistory,Long> {
}

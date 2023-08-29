package com.example.matchdomain.donation.repository;


import com.example.matchdomain.donation.entity.PaymentStatus;
import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.donation.entity.RequestPaymentHistory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RegularPaymentRepository extends JpaRepository<RegularPayment,Long> {
    @EntityGraph(attributePaths = "userCard")
    List<RegularPayment> findByPayDate(int dayOfMonth);

    @EntityGraph(attributePaths = "userCard")
    List<RegularPayment> findByPayDateGreaterThanEqual(int currentDay);

}

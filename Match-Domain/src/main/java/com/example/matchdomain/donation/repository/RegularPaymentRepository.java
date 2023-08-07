package com.example.matchdomain.donation.repository;


import com.example.matchdomain.donation.entity.RegularPayment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RegularPaymentRepository extends JpaRepository<RegularPayment,Long> {
}

package com.example.matchdomain.donationTemporary.repository;

import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donationTemporary.entity.Deposit;
import com.example.matchdomain.donationTemporary.entity.DonationTemporary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationTemporaryRepository extends JpaRepository<DonationTemporary,Long> {
    Page<DonationTemporary> findByStatusOrderByCreatedAtDesc(Status status, Pageable pageable);

    Page<DonationTemporary> findByDepositOrderByCreatedAtDesc(Deposit deposit, Pageable pageable);

    Page<DonationTemporary> findByNameContainingAndDepositOrderByCreatedAtDesc(String content, Deposit deposit, Pageable pageable);

    Page<DonationTemporary> findByNameContainingOrderByCreatedAtDesc(String content, Pageable pageable);
}

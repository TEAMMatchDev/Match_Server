package com.example.matchdomain.donationTemporary.repository;

import com.example.matchdomain.donationTemporary.entity.Deposit;
import com.example.matchdomain.donationTemporary.entity.DonationKind;
import com.example.matchdomain.donationTemporary.entity.DonationList;
import com.example.matchdomain.donationTemporary.entity.DonationTemporary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationListRepository extends JpaRepository<DonationList,Long> {
    Page<DonationList> findByDonationTemporary_DonationKindAndDonationTemporary_DepositOrderByCreatedAtDesc(DonationKind donationKind, Deposit deposit, Pageable pageable);
}

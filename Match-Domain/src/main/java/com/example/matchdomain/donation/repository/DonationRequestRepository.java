package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.DonationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRequestRepository extends JpaRepository<DonationRequest,Long> {
}

package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.DonationExecution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DonationExecutionRepository extends JpaRepository<DonationExecution, Long>, ExecutionCustomRepository {
    List<DonationExecution> findTopByDonationUser_User_IdOrderByCreatedAtDesc(Long id);
}

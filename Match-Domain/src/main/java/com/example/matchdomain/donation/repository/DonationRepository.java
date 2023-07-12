package com.example.matchdomain.donation.repository;


import com.example.matchdomain.donation.entity.DonationProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepository extends JpaRepository<DonationProject,Long> {

}

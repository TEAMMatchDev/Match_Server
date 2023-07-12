package com.example.matchdomain.donation.repository;


import com.example.matchdomain.donation.entity.DonationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationUserRepository extends JpaRepository<DonationUser,Long> {

}

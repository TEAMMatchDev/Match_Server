package com.example.matchdomain.review.repository;

import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewCustomRepository {
    Optional<Review> findByDonationUser(DonationUser donationUser);
}

package com.example.matchdomain.review.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.review.entity.Review;
import com.example.matchdomain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Adaptor
@RequiredArgsConstructor
public class ReviewAdaptor {
    private final ReviewRepository reviewRepository;


    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    public Optional<Review> findByDonationUser(DonationUser donationUser) {
        return reviewRepository.findByDonationUser(donationUser);
    }
}

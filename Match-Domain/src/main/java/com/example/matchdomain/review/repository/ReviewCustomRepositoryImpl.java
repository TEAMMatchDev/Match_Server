package com.example.matchdomain.review.repository;

import com.example.matchdomain.donation.entity.QDonationExecution;
import com.example.matchdomain.donation.entity.QDonationUser;
import com.example.matchdomain.project.entity.QProject;
import com.example.matchdomain.review.entity.QReview;
import com.example.matchdomain.review.entity.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository{
    private final JPAQueryFactory queryFactory;

}

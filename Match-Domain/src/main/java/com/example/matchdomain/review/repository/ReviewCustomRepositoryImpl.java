package com.example.matchdomain.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository{
    private final JPAQueryFactory queryFactory;

}

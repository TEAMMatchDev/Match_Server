package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.DonationExecution;
import com.example.matchdomain.donation.entity.QDonationExecution;
import com.example.matchdomain.donation.entity.QDonationUser;
import com.example.matchdomain.project.entity.QProject;
import com.example.matchdomain.review.entity.QReview;
import com.example.matchdomain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ExecutionCustomRepositoryImpl implements ExecutionCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<DonationExecution> getDonationExecutionForPopUp(User user) {
        QDonationExecution qDonationExecution = QDonationExecution.donationExecution;
        QDonationUser qDonationUser = QDonationUser.donationUser;
        QReview qReview = QReview.review;

        return queryFactory
                .select(qDonationExecution)
                .from(qDonationExecution)
                .leftJoin(qDonationUser).on(qDonationExecution.id.eq(qDonationUser.id)).fetchJoin()
                .leftJoin(qReview).on(qDonationExecution.id.eq(qReview.donationExecution.id))
                .where(qReview.isNull().and(qDonationExecution.donationUser.userId.eq(user.getId())))
                .orderBy(qDonationExecution.createdAt.desc())
                .limit(1)
                .fetch();
    }

    @Override
    public DonationExecution findByExecutionId(Long executionId) {
        QDonationExecution qDonationExecution = QDonationExecution.donationExecution;
        QDonationUser qDonationUser = QDonationUser.donationUser;
        QProject qProject = QProject.project;

        return queryFactory.select(qDonationExecution)
                .from(qDonationExecution)
                .join(qDonationUser).on(qDonationExecution.donationUser.id.eq(qDonationUser.id))
                .join(qProject).on(qDonationUser.project.id.eq(qProject.id))
                .where(qDonationExecution.id.eq(executionId))
                .fetchOne();
    }

}

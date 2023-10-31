package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.QDonationUser;
import com.example.matchdomain.donation.entity.QRegularPayment;
import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.donation.entity.enums.RegularPayStatus;
import com.example.matchdomain.user.entity.User;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class RegularPaymentCustomRepositoryImpl implements RegularPaymentCustomRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<RegularPayment> findRegularListCustom(User user, RegularPayStatus regularPayStatus, Pageable pageable) {
        QRegularPayment regularPayment = QRegularPayment.regularPayment;
        QDonationUser donationUser = QDonationUser.donationUser;

        List<RegularPayment> regularPayments =
                queryFactory.selectFrom(regularPayment)
                        .leftJoin(donationUser).on(regularPayment.id.eq(donationUser.regularPaymentId))
                        .where(donationUser.createdAt.eq(
                                JPAExpressions
                                        .select(donationUser.createdAt.max())
                                        .from(donationUser)
                                        .where(donationUser.regularPayment.eq(regularPayment)))
                                .and(regularPayment.user.eq(user))
                                .and(regularPayment.regularPayStatus.eq(regularPayStatus))

                        )
                        .orderBy(regularPayment.createdAt.asc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();
        JPAQuery<RegularPayment> countQuery = queryFactory
                .selectFrom(regularPayment)
                .where(regularPayment.user.eq(user)
                .and(regularPayment.regularPayStatus.eq(regularPayStatus)));

        return PageableExecutionUtils.getPage(regularPayments, pageable, countQuery::fetchCount);
    }
}

package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class DonationCustomRepositoryImpl implements DonationCustomRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<DonationHistory> getDonationHistoryCustom(Long regularPaymentId, Long donationId, HistoryStatus historyStatus, Pageable pageable) {
        QDonationHistory donationHistory = QDonationHistory.donationHistory;
        QRegularPayment regularPayment = QRegularPayment.regularPayment;
        QDonationUser donationUser = QDonationUser.donationUser;


        List<DonationHistory> donationHistories = queryFactory.select(donationHistory)
                .from(donationHistory)
                .join(donationHistory).on(donationHistory.donationUserId.eq(donationId))
                .join(regularPayment).on(donationUser.regularPaymentId.eq(regularPayment.id))
                .where(
                        donationHistory.donationUserId.eq(donationId).and(donationHistory.historyStatus.eq(HistoryStatus.CREATE))
                                .or(regularPayment.id.eq(regularPaymentId).and(donationHistory.historyStatus.ne(HistoryStatus.CREATE)))
                                .and(donationHistory.historyStatus.ne(HistoryStatus.TURN_ON))
                )
                .orderBy(donationHistory.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<DonationHistory> countQuery = queryFactory.selectFrom(donationHistory)
                .join(donationHistory).on(donationHistory.donationUserId.eq(donationId))
                .join(regularPayment).on(donationUser.regularPaymentId.eq(regularPayment.id))
                .where(
                        donationHistory.donationUserId.eq(donationId).and(donationHistory.historyStatus.eq(HistoryStatus.CREATE))
                                .or(regularPayment.id.eq(regularPaymentId).and(donationHistory.historyStatus.ne(HistoryStatus.CREATE)))
                                .and(donationHistory.historyStatus.ne(HistoryStatus.TURN_ON))
                ).orderBy(donationHistory.createdAt.asc());
        return PageableExecutionUtils.getPage(donationHistories, pageable, countQuery::fetchCount);
    }
}

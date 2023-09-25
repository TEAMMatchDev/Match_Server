package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.DonationHistory;
import com.example.matchdomain.donation.entity.HistoryStatus;
import com.example.matchdomain.donation.entity.QDonationHistory;
import com.example.matchdomain.donation.entity.QRegularPayment;
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

        List<DonationHistory> donationHistories = queryFactory.select(donationHistory)
                .from(donationHistory)
                .join(regularPayment).on(donationHistory.regularPaymentId.eq(regularPayment.id))
                .where(
                        donationHistory.donationUserId.eq(161L).and(donationHistory.historyStatus.eq(HistoryStatus.CREATE))
                                .or(regularPayment.id.eq(3L).and(donationHistory.historyStatus.ne(HistoryStatus.CREATE)))
                )
                .orderBy(donationHistory.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<DonationHistory> countQuery = queryFactory.selectFrom(donationHistory)
                .join(regularPayment).on(donationHistory.regularPaymentId.eq(regularPayment.id))
                .where(
                        donationHistory.donationUserId.eq(161L).and(donationHistory.historyStatus.eq(HistoryStatus.CREATE))
                                .or(regularPayment.id.eq(3L).and(donationHistory.historyStatus.ne(HistoryStatus.CREATE)))
                ).orderBy(donationHistory.createdAt.asc());
        return PageableExecutionUtils.getPage(donationHistories, pageable, countQuery::fetchCount);
    }
}

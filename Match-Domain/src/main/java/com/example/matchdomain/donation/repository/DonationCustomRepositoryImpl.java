package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.*;
import com.example.matchdomain.donation.entity.enums.HistoryStatus;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.matchdomain.donation.entity.enums.HistoryStatus.COMPLETE;

@RequiredArgsConstructor
public class DonationCustomRepositoryImpl implements DonationCustomRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<DonationHistory> getDonationHistoryCustom(Long regularPaymentId, Long donationId, HistoryStatus historyStatus, Pageable pageable, Long projectId) {
        QDonationHistory donationHistory = QDonationHistory.donationHistory;
        QRegularPayment regularPayment = QRegularPayment.regularPayment;
        QDonationUser donationUser = QDonationUser.donationUser;


        List<DonationHistory> donationHistories = queryFactory.select(donationHistory)
                .from(donationHistory)
                .leftJoin(donationUser).on(donationUser.id.eq(donationHistory.donationUserId))
                .where(
                        donationHistory.donationUserId.eq(donationId)
                                .or(donationHistory.projectId.eq(projectId))
                                .and(donationHistory.historyStatus.ne(HistoryStatus.TURN_ON))
                                .and(donationHistory.historyStatus.ne(HistoryStatus.START))
                                .and(donationHistory.historyStatus.ne(HistoryStatus.FINISH))
                                .and(
                                        donationHistory.historyStatus.eq(HistoryStatus.COMPLETE)
                                                .and(donationHistory.completeIdLists.contains(donationId))
                                                .or(donationHistory.historyStatus.ne(HistoryStatus.COMPLETE)))
                                .and(
                                        donationHistory.historyStatus.eq(HistoryStatus.CHANGE)
                                                .and(donationHistory.changeIdLists.contains(donationId))
                                                .or(donationHistory.historyStatus.ne(HistoryStatus.CHANGE)))

                )
                .orderBy(donationHistory.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<DonationHistory> countQuery = queryFactory.selectFrom(donationHistory)
                .leftJoin(donationUser).on(donationUser.id.eq(donationHistory.donationUserId))
                .where(
                        donationHistory.donationUserId.eq(donationId)
                                .or(donationHistory.projectId.eq(projectId))
                                .and(donationHistory.historyStatus.ne(HistoryStatus.TURN_ON))
                                .and(donationHistory.historyStatus.ne(HistoryStatus.START))
                                .and(donationHistory.historyStatus.ne(HistoryStatus.FINISH))
                                .and(
                                        donationHistory.historyStatus.eq(HistoryStatus.COMPLETE)
                                                .and(donationHistory.completeIdLists.contains(donationId))
                                                .or(donationHistory.historyStatus.ne(HistoryStatus.COMPLETE)))
                                .and(
                                        donationHistory.historyStatus.eq(HistoryStatus.CHANGE)
                                                .and(donationHistory.changeIdLists.contains(donationId))
                                                .or(donationHistory.historyStatus.ne(HistoryStatus.CHANGE)))

                )
                .orderBy(donationHistory.createdAt.asc());
        return PageableExecutionUtils.getPage(donationHistories, pageable, countQuery::fetchCount);
    }
}

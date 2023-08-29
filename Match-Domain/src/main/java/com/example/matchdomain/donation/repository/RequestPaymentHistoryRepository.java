package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.PaymentStatus;
import com.example.matchdomain.donation.entity.RequestPaymentHistory;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestPaymentHistoryRepository extends JpaRepository<RequestPaymentHistory,Long> {
    List<RequestPaymentHistory> findByPayDateGreaterThanEqualAndPaymentStatus(int currentDay, PaymentStatus paymentStatus);

    List<RequestPaymentHistory> findByPayDateAndPaymentStatus(int currentDay, PaymentStatus paymentStatus);


    @EntityGraph(attributePaths = {"user","regularPayment","userCard"})
    @Query("SELECT rph FROM RequestPaymentHistory rph " +
            "left join fetch RegularPayment rp on rph.regularPaymentId = rp.id " +
            "left join fetch UserCard uc on uc.id = rp.userCardId " +
            "left join fetch User u on rph.userId = u.id " +
            "where rph.paymentStatus=:paymentStatus")
    List<RequestPaymentHistory> findByPaymentStatus(@Param("paymentStatus") PaymentStatus paymentStatus);
}

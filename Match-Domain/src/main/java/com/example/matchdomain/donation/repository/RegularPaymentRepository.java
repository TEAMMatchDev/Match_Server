package com.example.matchdomain.donation.repository;


import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.RegularPayment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RegularPaymentRepository extends JpaRepository<RegularPayment,Long> {

    Optional<RegularPayment> findByIdAndStatus(Long regularId, Status status);

    @EntityGraph(attributePaths = "userCard")
    List<RegularPayment> findByPayDateGreaterThanEqualAndStatus(int currentDay, Status status);

    @EntityGraph(attributePaths = "userCard")
    List<RegularPayment> findByPayDateAndStatus(int currentDay, Status status);
}

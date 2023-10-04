package com.example.matchdomain.donation.repository;


import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.DonationStatus;
import com.example.matchdomain.donation.entity.RegularPayStatus;
import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.project.entity.ProjectStatus;
import com.example.matchdomain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface RegularPaymentRepository extends JpaRepository<RegularPayment,Long> {

    Optional<RegularPayment> findByIdAndStatus(Long regularId, Status status);

    @EntityGraph(attributePaths = "userCard")
    List<RegularPayment> findByPayDateGreaterThanEqualAndStatus(int currentDay, Status status);

    @EntityGraph(attributePaths = "userCard")
    List<RegularPayment> findByPayDateAndStatus(int currentDay, Status status);

    List<RegularPayment> findByPayDateGreaterThanEqualAndStatusAndRegularPayStatus(int currentDay, Status status, RegularPayStatus regularPayStatus);

    @Query("SELECT RP FROM RegularPayment RP join fetch RP.user where RP.projectId=:id and RP.regularPayStatus=:regularPayStatus")
    List<RegularPayment> findByProjectIdAndRegularPayStatus(@Param("id") Long id,@Param("regularPayStatus") RegularPayStatus regularPayStatus);

    List<RegularPayment> findByUser(User user);

    List<RegularPayment> findByUserCardId(Long cardId);
}

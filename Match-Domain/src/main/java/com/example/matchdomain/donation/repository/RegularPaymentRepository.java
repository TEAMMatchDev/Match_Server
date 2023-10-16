package com.example.matchdomain.donation.repository;


import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.enums.RegularPayStatus;
import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface RegularPaymentRepository extends JpaRepository<RegularPayment,Long>, RegularPaymentCustomRepository {

    Optional<RegularPayment> findByIdAndStatus(Long regularId, Status status);

    List<RegularPayment> findByPayDateGreaterThanEqualAndStatusAndRegularPayStatus(int currentDay, Status status, RegularPayStatus regularPayStatus);

    @Query("SELECT RP FROM RegularPayment RP join fetch RP.user where RP.projectId=:id and RP.regularPayStatus=:regularPayStatus")
    List<RegularPayment> findByProjectIdAndRegularPayStatus(@Param("id") Long id,@Param("regularPayStatus") RegularPayStatus regularPayStatus);

    List<RegularPayment> findByUser(User user);
    
    List<RegularPayment> findByUserCardId(Long cardId);

    Page<RegularPayment> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    @Query(value = "select RP.id as 'regularPayId', P.usages as 'usages', DU.inherenceName as 'inherenceName', DU.flameImage as 'image'" +
            "from RegularPayment RP " +
            "join Project P on P.id = RP.projectId " +
            "join DonationUser DU on DU.regularPaymentId = RP.id " +
            "where RP.userId = :userId and regularPayStatus = :regularPayStatus and DU.createdAt = " +
            "(select max(DU2.createdAt) from DonationUser DU2 where DU2.regularPaymentId = RP.id )",
            nativeQuery = true,
            countQuery = "select count(*) from RegularPayment RP where RP.userId = :userId and regularPayStatus = :regularPayStatus")
    Page<RegularPaymentFlame> getBurningFlameListCustom(@Param("userId") Long userId, @Param("regularPayStatus") String regularPayStatus,
                                                        Pageable pageable);

    List<RegularPayment> findByIdAndUserOrderByCreatedAtDesc(Long regularPayId, User user);

    interface RegularPaymentFlame {
        Long getRegularPayId();

        String getUsages();

        String getInherenceName();

        String getImage();

    }
}

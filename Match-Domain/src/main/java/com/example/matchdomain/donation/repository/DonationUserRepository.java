package com.example.matchdomain.donation.repository;


import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.DonationStatus;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DonationUserRepository extends JpaRepository<DonationUser,Long> {
    @EntityGraph(attributePaths = {"user"})
    List<DonationUser> findByUser(User user);

    boolean existsByInherenceName(String randomName);

    Page<DonationUser> findByUserId(Long userId, Pageable pageable);




    List<DonationUser> findByUserAndDonationStatusNot(User user, DonationStatus donationStatus);


    Page<DonationUser> findByUserIdAndDonationStatusNotOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByPriceAsc(Long id, DonationStatus donationStatus, String content, String s, String content1, Status active, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusNotOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByPriceDesc(Long id, DonationStatus donationStatus, String content, String s, String content1, Status active, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusNotOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByCreatedAtAsc(Long id, DonationStatus donationStatus, String content, String s, String content1, Status active, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusNotOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByCreatedAtDesc(Long id, DonationStatus donationStatus, String content, String s, String content1, Status active, Pageable pageable);


    List<DonationUser> findByUserAndDonationStatusNotAndStatus(User user, DonationStatus donationStatus, Status status);

    Page<DonationUser> findByUserIdAndDonationStatusAndStatusOrderByCreatedAtDesc(Long userId,  DonationStatus donationStatus,Status status, Pageable pageable);


    Page<DonationUser> findByUserIdAndDonationStatusNotAndStatusOrderByCreatedAtAsc(Long id, DonationStatus donationStatus, Status status, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusNotAndStatusOrderByCreatedAtDesc(Long id, DonationStatus donationStatus, Status status, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusNotAndStatusOrderByPriceDesc(Long id, DonationStatus donationStatus, Status status, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusNotAndStatusOrderByPriceAsc(Long id, DonationStatus donationStatus, Status status, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusAndStatusOrderByCreatedAtAsc(Long id, DonationStatus donationStatus, Status status, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusAndStatusOrderByPriceDesc(Long id, DonationStatus donationStatus, Status status, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusAndStatusOrderByPriceAsc(Long id, DonationStatus donationStatus, Status status, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByCreatedAtAsc(Long id, DonationStatus donationStatus, String content, String content1, String content2, Status status, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByCreatedAtDesc(Long id, DonationStatus donationStatus, String content, String content1, String content2, Status status, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByPriceDesc(Long id, DonationStatus donationStatus, String content, String content1, String content2, Status status, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingAndStatusOrderByPriceAsc(Long id, DonationStatus donationStatus, String content, String content1, String content2, Status status, Pageable pageable);

    Page<DonationUser> findByUserIdAndStatusAndDonationStatusNotOrderByCreatedAtDesc(Long userId, Status status, DonationStatus donationStatus, Pageable pageable);

    @Query(value = "SELECT DU FROM DonationUser DU JOIN FETCH DU.user " +
            "where DU.projectId=:projectId",
    countQuery = "SELECT count(DU) FROM DonationUser DU where DU.projectId=:projectId")
    Page<DonationUser> findByProjectId(@Param("projectId") Long projectId, Pageable pageable);

    List<DonationUser> findByDonationStatusNot(DonationStatus donationStatus);
}

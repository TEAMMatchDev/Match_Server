package com.example.matchdomain.donation.repository;


import com.example.matchdomain.donation.entity.DonationStatus;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationUserRepository extends JpaRepository<DonationUser,Long> {
    @EntityGraph(attributePaths = {"user"})
    List<DonationUser> findByUser(User user);

    boolean existsByInherenceName(String randomName);

    Page<DonationUser> findByUserId(Long userId, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusNotOrderByCreatedAtAsc(Long userId, DonationStatus donationStatus, Pageable pageable);


    Page<DonationUser> findByUserIdAndDonationStatusNot(Long userId, DonationStatus donationStatus ,Pageable pageable);

    List<DonationUser> findByUserAndDonationStatusNot(User user, DonationStatus donationStatus);

    Page<DonationUser> findByUserIdAndDonationStatus(Long userId, DonationStatus donationStatus, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusNotOrderByCreatedAtDesc(Long id, DonationStatus donationStatus, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusNotOrderByPriceDesc(Long id, DonationStatus donationStatus, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusNotOrderByPriceAsc(Long id, DonationStatus donationStatus, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusNotOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingOrderByPriceAsc(Long id, DonationStatus donationStatus, String content, String s, String content1, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusNotOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingOrderByPriceDesc(Long id, DonationStatus donationStatus, String content, String s, String content1, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusNotOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingOrderByCreatedAtAsc(Long id, DonationStatus donationStatus, String content, String s, String content1, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusNotOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingOrderByCreatedAtDesc(Long id, DonationStatus donationStatus, String content, String s, String content1, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusOrderByCreatedAtDesc(Long id, DonationStatus donationStatus, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusOrderByCreatedAtAsc(Long id, DonationStatus donationStatus, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusOrderByPriceDesc(Long id, DonationStatus donationStatus, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusOrderByPriceAsc(Long id, DonationStatus donationStatus, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingOrderByCreatedAtDesc(Long id, DonationStatus donationStatus, String content, String content1, String content2, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingOrderByCreatedAtAsc(Long id, DonationStatus donationStatus, String content, String content1, String content2, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingOrderByPriceDesc(Long id, DonationStatus donationStatus, String content, String content1, String content2, Pageable pageable);

    Page<DonationUser> findByUserIdAndDonationStatusOrProject_UsagesContainingOrProject_ProjectNameContainingOrProject_ProjectExplanationContainingOrderByPriceAsc(Long id, DonationStatus donationStatus, String content, String content1, String content2, Pageable pageable);
}

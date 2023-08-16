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
    Page<DonationUser> findByUserIdAndDonationStatusNot(Long userId, DonationStatus donationStatus ,Pageable pageable);

    List<DonationUser> findByUserAndDonationStatusNot(User user, DonationStatus donationStatus);

    Page<DonationUser> findByUserIdAndDonationStatus(Long userId, DonationStatus donationStatus, Pageable pageable);
}

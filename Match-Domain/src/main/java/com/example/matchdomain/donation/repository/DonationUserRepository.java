package com.example.matchdomain.donation.repository;


import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationUserRepository extends JpaRepository<DonationUser,Long> {
    @EntityGraph(attributePaths = {"user"})
    List<DonationUser> findByUser(User user);

    boolean existsByInherenceName(String randomName);
}

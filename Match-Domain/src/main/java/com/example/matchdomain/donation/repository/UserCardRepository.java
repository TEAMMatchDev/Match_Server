package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.UserCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCardRepository extends JpaRepository<UserCard,Long> {
}

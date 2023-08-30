package com.example.matchdomain.donation.repository;

import com.example.matchdomain.donation.entity.UserCard;
import com.example.matchdomain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCardRepository extends JpaRepository<UserCard,Long> {
    List<UserCard> findByUser(User user);
}

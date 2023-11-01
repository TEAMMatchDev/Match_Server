package com.example.matchdomain.donation.repository;

import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.UserCard;
import com.example.matchdomain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCardRepository extends JpaRepository<UserCard,Long> {
    List<UserCard> findByUser(User user);

    List<UserCard> findByUserAndStatus(User user, Status status);

    Optional<UserCard> findByIdAndStatus(Long cardId, Status status);

    List<UserCard> findByUserIdAndStatus(Long userId, Status status);
}

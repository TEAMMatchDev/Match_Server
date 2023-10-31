package com.example.matchdomain.notification.repository;

import com.example.matchdomain.notification.entity.Notification;
import com.example.matchdomain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByUserOrderByCreatedAt(User user, Pageable pageable);

    int countByUserAndIsRead(User user, boolean b);
}

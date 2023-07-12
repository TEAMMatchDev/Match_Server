package com.example.matchdomain.user.repository;

import com.example.matchdomain.user.entity.UserFcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFcmTokenRepository extends JpaRepository<UserFcmToken,Long> {

}

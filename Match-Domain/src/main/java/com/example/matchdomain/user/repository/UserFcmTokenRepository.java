package com.example.matchdomain.user.repository;

import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.UserFcmToken;
import com.example.matchdomain.user.entity.enums.Alarm;
import com.example.matchdomain.user.entity.pk.UserFcmPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFcmTokenRepository extends JpaRepository<UserFcmToken, UserFcmPk> {

    List<UserFcmToken> findByUserFcmPk_UserIdAndUser_ServiceAlarm(Long id, Alarm alarm);

    List<UserFcmToken> findByUserIdAndUser_ServiceAlarm(Long id, Alarm alarm);
}

package com.example.matchdomain.user.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.user.entity.UserFcmToken;
import com.example.matchdomain.user.repository.UserFcmTokenRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.matchdomain.user.entity.enums.Alarm.ACTIVE;

@Adaptor
@RequiredArgsConstructor
public class UserFcmAdaptor {
    private final UserFcmTokenRepository fcmTokenRepository;

    public List<UserFcmToken> findByUser(Long userId) {
        return fcmTokenRepository.findByUserIdAndUser_ServiceAlarm(userId, ACTIVE);
    }
}

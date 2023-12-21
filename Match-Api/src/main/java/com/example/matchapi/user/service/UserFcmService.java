package com.example.matchapi.user.service;

import com.example.matchapi.user.converter.UserConverter;
import com.example.matchapi.user.dto.UserReq;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.pk.UserFcmPk;
import com.example.matchdomain.user.repository.UserFcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserFcmService {
    private final UserFcmTokenRepository userFcmTokenRepository;
    private final UserConverter userConverter;

    public void saveFcmToken(User user, UserReq.FcmToken token) {
        userFcmTokenRepository.save(userConverter.convertToUserFcm(user, token));
    }

    public void deleteFcmToken(Long userId, String deviceId) {
        UserFcmPk userFcmPk = UserFcmPk.builder().userId(userId).deviceId(deviceId).build();
        if(userFcmTokenRepository.existsById(userFcmPk)) {
            userFcmTokenRepository.deleteById(userFcmPk);
        }
    }
}

package com.example.matchapi.user.service;

import com.example.matchcommon.exception.BadRequestException;
import com.example.matchdomain.redis.entity.RefreshToken;
import com.example.matchdomain.redis.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.matchdomain.user.exception.UserAuthErrorCode.INVALID_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findRefreshToken(Long userId) {
        return refreshTokenRepository.findById(String.valueOf(userId)).orElseThrow(()-> new BadRequestException(INVALID_REFRESH_TOKEN));
    }
}

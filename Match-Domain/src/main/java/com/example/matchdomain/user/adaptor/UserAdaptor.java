package com.example.matchdomain.user.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.enums.SocialType;
import com.example.matchdomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

@Adaptor
@RequiredArgsConstructor
public class UserAdaptor {
    private final UserRepository userRepository;
    public Optional<User> existsSocialUser(String socialId, SocialType socialType){
        return userRepository.findBySocialIdAndSocialType(socialId, socialType);
    }

    @Cacheable(value = "userCache", key = "#userId", cacheManager = "redisCacheManager")
    public Optional<User> findByUserId(Long userId) {
        System.out.println("캐싱 하자");
        return userRepository.findById(userId);
    }
}

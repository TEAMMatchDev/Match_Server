package com.example.matchdomain.user.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.enums.SocialType;
import com.example.matchdomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static com.example.matchdomain.user.entity.enums.Alarm.ACTIVE;

@Adaptor
@RequiredArgsConstructor
public class UserAdaptor {
    private final UserRepository userRepository;
    public Optional<User> existsSocialUser(String socialId, SocialType socialType){
        return userRepository.findBySocialIdAndSocialType(socialId, socialType);
    }

    public Optional<User> findByUserId(Long userId) {
        return userRepository.findById(userId);
    }

    public List<User> findByServiceAlarm() {
        return userRepository.findByServiceAlarm(ACTIVE);
    }
}

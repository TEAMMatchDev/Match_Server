package com.example.matchdomain.user.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.enums.SocialType;
import com.example.matchdomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Adaptor
@RequiredArgsConstructor
public class UserAdaptor {
    private final UserRepository userRepository;
    public Optional<User> existsSocialUser(String socialId, SocialType socialType){
        return userRepository.findBySocialIdAndSocialType(socialId, socialType);
    }
}

package com.example.matchdomain.user.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.enums.SocialType;
import com.example.matchdomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.example.matchdomain.user.entity.enums.Alarm.ACTIVE;
import static com.example.matchdomain.user.exception.UserLoginErrorCode.NOT_EXIST_USER;

@Adaptor
@RequiredArgsConstructor
public class UserAdaptor {
    private final UserRepository userRepository;
    public Optional<User> existsSocialUser(String socialId, SocialType socialType){
        return userRepository.findBySocialIdAndSocialTypeAndStatus(socialId, socialType, Status.ACTIVE);
    }

    public Optional<User> findByUserId(Long userId) {
        return userRepository.findById(userId);
    }

    public List<User> findByServiceAlarm() {
        return userRepository.findByServiceAlarm(ACTIVE);
    }

    public List<User> findDeleteUsers() {
        return userRepository.findByStatus(Status.INACTIVE);
    }

    public User findByUser(String userId) {
        return userRepository.findByIdAndStatus(Long.valueOf(userId), Status.ACTIVE).orElseThrow(()->new BadRequestException(NOT_EXIST_USER));
    }

    public boolean existsEmail(String email) {
        return userRepository.existsByEmailAndStatus(email, Status.ACTIVE);
    }

    public boolean existsPhoneNumber(String phone) {
        return userRepository.existsByPhoneNumberAndStatus(phone, Status.ACTIVE);
    }

    public User findByUserName(String email) {
        return userRepository.findByUsernameAndStatus(email, Status.ACTIVE).orElseThrow(()->new BadRequestException(NOT_EXIST_USER));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean checkEmailPassword(String email, SocialType socialType) {
        return userRepository.existsByUsernameAndSocialTypeAndStatus(email, socialType, Status.ACTIVE);
    }
}

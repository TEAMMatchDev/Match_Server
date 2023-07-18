package com.example.matchapi.user.service;

import com.example.matchapi.user.convertor.UserConvertor;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.UserAddress;
import com.example.matchdomain.user.repository.UserAddressRepository;
import com.example.matchdomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final UserConvertor userConvertor;

    public Optional<User> findUser(long id) {
        return userRepository.findById(id);
    }

    public List<UserAddress> findUserAddress(Long id) {
        List<UserAddress> userAddressEntity = userAddressRepository.findByUserId(id);
        System.out.println(userAddressEntity);
        return userAddressEntity;
    }

    public UserRes.MyPage getMyPage(User user) {
        return userConvertor.toMyPage(user);
    }
}

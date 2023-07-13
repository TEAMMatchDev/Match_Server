package com.example.matchdomain.user.repository;

import com.example.matchdomain.user.entity.SocialType;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.UserAddress;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByUsername(String userName);


    Optional<User> findById(Long id);

    Optional<User> findBySocialIdAndSocialType(String id, SocialType kakao);


    Optional<User> findByPhoneNumberAndSocialTypeNot(String phoneNumber, SocialType socialType);
}

package com.example.matchdomain.redis.repository;

import com.example.matchdomain.redis.entity.AccessToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {
    Optional<AccessToken> findByToken(String token);
}

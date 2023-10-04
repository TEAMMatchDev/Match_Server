package com.example.matchdomain.user.repository;

import com.example.matchdomain.user.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress,Long> {
    List<UserAddress> findByUserId(Long id);
}

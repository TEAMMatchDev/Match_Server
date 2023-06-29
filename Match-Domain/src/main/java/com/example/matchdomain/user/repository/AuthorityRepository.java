package com.example.matchdomain.user.repository;


import com.example.matchdomain.user.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
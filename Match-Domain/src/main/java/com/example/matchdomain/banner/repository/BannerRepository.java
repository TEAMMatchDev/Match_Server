package com.example.matchdomain.banner.repository;

import com.example.matchdomain.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findAllByOrderByCreatedAtDesc();
}

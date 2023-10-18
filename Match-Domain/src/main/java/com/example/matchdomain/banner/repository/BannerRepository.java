package com.example.matchdomain.banner.repository;

import com.example.matchdomain.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
}

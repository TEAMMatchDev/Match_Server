package com.example.matchdomain.banner.repository;

import com.example.matchdomain.banner.entity.Banner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findAllByOrderByCreatedAtDesc();

    List<Banner> findByStartDateLessThanAndEndDateGreaterThanOrderByCreatedAtDesc(LocalDateTime now, LocalDateTime now1);

	Page<Banner> findByOrderByCreatedAtDesc(Pageable pageable);
}

package com.example.matchdomain.banner.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.banner.entity.Banner;
import com.example.matchdomain.banner.repository.BannerRepository;
import com.example.matchdomain.keyword.entity.SearchKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Adaptor
@RequiredArgsConstructor
public class BannerAdaptor {
    private final BannerRepository bannerRepository;

    public List<Banner> getBannerList() {
        LocalDateTime now = LocalDateTime.now();
        return bannerRepository.findByStartDateLessThanAndEndDateGreaterThanOrderByCreatedAtDesc(now, now);
    }

    public Banner save(Banner banner) {
        return bannerRepository.save(banner);
    }

    public Banner findById(Long bannerId) {
        return bannerRepository.findById(bannerId).orElseThrow();
    }

    public void deleteById(Long bannerId) {
        bannerRepository.deleteById(bannerId);
    }
}

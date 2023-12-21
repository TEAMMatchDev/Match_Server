package com.example.matchdomain.banner.adaptor;

import static com.example.matchdomain.banner.exception.BannerGerErrorCode.*;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchdomain.banner.entity.Banner;
import com.example.matchdomain.banner.repository.BannerRepository;
import com.example.matchdomain.keyword.entity.SearchKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
        return bannerRepository.findById(bannerId).orElseThrow(() -> new NotFoundException(NOT_EXISTS_BANNER));
    }

    public void deleteById(Long bannerId) {
        bannerRepository.deleteById(bannerId);
    }

	public Page<Banner> getBannerLists(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bannerRepository.findByOrderByCreatedAtDesc(pageable);

	}
}

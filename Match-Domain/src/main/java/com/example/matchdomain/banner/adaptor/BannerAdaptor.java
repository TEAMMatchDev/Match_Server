package com.example.matchdomain.banner.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.banner.entity.Banner;
import com.example.matchdomain.banner.repository.BannerRepository;
import com.example.matchdomain.keyword.entity.SearchKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.List;

@Adaptor
@RequiredArgsConstructor
public class BannerAdaptor {
    private final BannerRepository bannerRepository;

    public List<Banner> getBannerList() {
        return bannerRepository.findAllByOrderByCreatedAtDesc();
    }
}

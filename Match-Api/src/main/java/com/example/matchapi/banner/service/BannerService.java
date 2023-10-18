package com.example.matchapi.banner.service;

import com.example.matchapi.banner.convertor.BannerConvertor;
import com.example.matchapi.banner.dto.BannerRes;
import com.example.matchdomain.banner.adaptor.BannerAdaptor;
import com.example.matchdomain.banner.entity.Banner;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerAdaptor bannerAdaptor;
    private final BannerConvertor bannerConvertor;

    @Cacheable(cacheNames = "bannerCache", key = "'all'")
    public List<BannerRes.BannerList> getBannerList() {
        List<Banner> banners = bannerAdaptor.getBannerList();
        return bannerConvertor.BannerList(banners);
    }
}

package com.example.matchdomain.banner.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;

@Adaptor
@RequiredArgsConstructor
public class BannerAdaptor {
    private final BannerRepository bannerRepository;
}

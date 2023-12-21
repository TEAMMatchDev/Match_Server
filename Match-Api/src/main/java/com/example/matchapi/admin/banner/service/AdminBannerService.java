package com.example.matchapi.admin.banner.service;

import com.example.matchapi.banner.converter.BannerConverter;
import com.example.matchapi.banner.dto.BannerReq;
import com.example.matchapi.banner.dto.BannerRes;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.banner.adaptor.BannerAdaptor;
import com.example.matchdomain.banner.entity.Banner;
import com.example.matchdomain.banner.enums.BannerType;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminBannerService {
    private final BannerConverter bannerConverter;
    private final S3UploadService s3UploadService;
    private final BannerAdaptor bannerAdaptor;

    @Transactional
    @CachePut(cacheNames = "bannerCache", key = "'all'", cacheManager = "ehcacheManager")
    public List<BannerRes.BannerList> uploadBanner(BannerType bannerType,
                                                   MultipartFile bannerImage,
                                                   BannerReq.BannerUpload bannerUploadDto) {
        String bannerImg = s3UploadService.uploadBannerImage(bannerImage);
        bannerAdaptor.save(bannerConverter.convertToBannerUpload(bannerType, bannerImg, bannerUploadDto));
        return cachingBannerList();
    }

    @CacheEvict(cacheNames = "bannerCache")
    public List<BannerRes.BannerList> cachingBannerList() {
        List<Banner> banners = bannerAdaptor.getBannerList();

        return bannerConverter.convertToBannerList(banners);
    }

    @Transactional
    public void deleteBanner(Long bannerId) {
        Banner banner = bannerAdaptor.findById(bannerId);
        s3UploadService.deleteFile(banner.getBannerImg());
        bannerAdaptor.deleteById(bannerId);
    }

    @Transactional
    public void patchBanner(Long bannerId, BannerReq.BannerPatchDto bannerPatchDto, MultipartFile bannerImage) {
        Banner banner = bannerAdaptor.findById(bannerId);
        if(bannerPatchDto.isEditImage()){
            s3UploadService.deleteFile(banner.getBannerImg());
            String imgUrl = s3UploadService.uploadBannerImage(bannerImage);
            banner.updateBanner(bannerPatchDto.getName(), banner.getStartDate(), banner.getEndDate(), imgUrl);
        }else{
            banner.updateBanner(bannerPatchDto.getName(), banner.getStartDate(), banner.getEndDate(), banner.getBannerImg());
        }
        bannerAdaptor.save(banner);
    }

    public PageResponse<List<BannerRes.BannerAdminListDto>> getBannerLists(int page, int size) {
        Page<Banner> banners = bannerAdaptor.getBannerLists(page, size);
        return new PageResponse<>(banners.isLast(), banners.getSize(), bannerConverter.convertToBannerLists(banners.getContent()));
    }
}

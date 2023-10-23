package com.example.matchapi.banner.convertor;

import com.example.matchapi.banner.dto.BannerReq;
import com.example.matchapi.banner.dto.BannerRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.banner.entity.Banner;
import com.example.matchdomain.banner.enums.BannerType;

import java.util.ArrayList;
import java.util.List;

@Convertor
public class BannerConvertor {

    public Banner BannerUpload(BannerType bannerType, String bannerImg, BannerReq.BannerUpload bannerUploadDto) {
        if(bannerType.equals(BannerType.EVENT)){
            return Banner
                    .builder()
                    .bannerImg(bannerImg)
                    .bannerType(bannerType)
                    .eventId(bannerUploadDto.getEventId())
                    .build();
        }
        else{
            return Banner
                    .builder()
                    .bannerImg(bannerImg)
                    .bannerType(bannerType)
                    .contentsUrl(bannerUploadDto.getContentsUrl())
                    .build();
        }
    }

    public List<BannerRes.BannerList> BannerList(List<Banner> banners) {
        List<BannerRes.BannerList> bannerLists = new ArrayList<>();

        banners.forEach(
                result ->
                        bannerLists.add(
                                BannerInfo(result)
                        )
        );

        return bannerLists;
    }

    private BannerRes.BannerList BannerInfo(Banner result) {
        return BannerRes.BannerList
                .builder()
                .bannerId(result.getId())
                .bannerType(result.getBannerType())
                .bannerImg(result.getBannerImg())
                .eventId(result.getEventId())
                .contentsUrl(result.getContentsUrl())
                .build();
    }
}

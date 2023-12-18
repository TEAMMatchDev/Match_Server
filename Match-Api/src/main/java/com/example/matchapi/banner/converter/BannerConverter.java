package com.example.matchapi.banner.converter;

import com.example.matchapi.banner.dto.BannerReq;
import com.example.matchapi.banner.dto.BannerRes;
import com.example.matchcommon.annotation.Converter;
import com.example.matchdomain.banner.entity.Banner;
import com.example.matchdomain.banner.enums.BannerType;

import java.util.ArrayList;
import java.util.List;

@Converter
public class BannerConverter {

    public Banner convertToBannerUpload(BannerType bannerType, String bannerImg, BannerReq.BannerUpload bannerUploadDto) {
        if(bannerType.equals(BannerType.EVENT)){
            return Banner
                    .builder()
                    .bannerImg(bannerImg)
                    .bannerType(bannerType)
                    .name(bannerUploadDto.getName())
                    .eventId(bannerUploadDto.getEventId())
                    .build();
        }
        else{
            return Banner
                    .builder()
                    .bannerImg(bannerImg)
                    .bannerType(bannerType)
                    .name(bannerUploadDto.getName())
                    .contentsUrl(bannerUploadDto.getContentsUrl())
                    .build();
        }
    }

    public List<BannerRes.BannerList> convertToBannerList(List<Banner> banners) {
        List<BannerRes.BannerList> bannerLists = new ArrayList<>();

        banners.forEach(
                result ->
                        bannerLists.add(
                                convertToBannerInfo(result)
                        )
        );

        return bannerLists;
    }

    private BannerRes.BannerList convertToBannerInfo(Banner result) {
        return BannerRes.BannerList
                .builder()
                .bannerId(result.getId())
                .bannerType(result.getBannerType())
                .bannerImg(result.getBannerImg())
                .eventId(result.getEventId())
                .contentsUrl(result.getContentsUrl())
                .build();
    }

    public List<BannerRes.BannerAdminListDto> convertToBannerLists(List<Banner> content) {
        List<BannerRes.BannerAdminListDto> bannerLists = new ArrayList<>();

        content.forEach(
                result ->
                        bannerLists.add(
                                convertToBannerListDto(result)
                        )
        );

        return bannerLists;
    }

    private BannerRes.BannerAdminListDto convertToBannerListDto(Banner result) {
        return BannerRes.BannerAdminListDto
            .builder()
            .bannerId(result.getId())
            .bannerType(result.getBannerType())
            .eventId(result.getEventId())
            .name(result.getName())
            .bannerImg(result.getBannerImg())
            .startDate(result.getStartDate())
            .endDate(result.getEndDate())
            .build();
    }
}

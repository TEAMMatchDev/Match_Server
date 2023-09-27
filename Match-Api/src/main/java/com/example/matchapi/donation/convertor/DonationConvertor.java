package com.example.matchapi.donation.convertor;

import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.helper.DonationHelper;
import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.*;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.donation.repository.HistoryImageRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.matchcommon.constants.MatchStatic.MATCH_NAME;
import static com.example.matchcommon.constants.MatchStatic.MATCH_PROFILE;
import static com.example.matchdomain.donation.entity.DonationStatus.*;

@Convertor
@RequiredArgsConstructor
public class DonationConvertor {
    private final DonationHelper donationHelper;
    private final HistoryImageRepository historyImageRepository;
    public DonationRes.FlameList Flame(DonationUser result) {
        return DonationRes.FlameList.builder()
                .donationId(result.getId())
                .amount(result.getPrice())
                .donationStatus(result.getDonationStatus().getName())
                .flameName(result.getInherenceName())
                .createdAt(result.getCreatedAt().getYear()+"년 "
                        +result.getCreatedAt().getMonthValue()+"월 "
                        +result.getCreatedAt().getDayOfMonth()+"일 "
                        +result.getCreatedAt().getHour()+"시 "
                        +result.getCreatedAt().getMinute()+"분")
                .build();
    }

    public DonationRes.DonationCount DonationCount(List<DonationUser> donationUserList) { int beforeCnt=0;
        int underCnt=0;
        int successCnt=0;

        for (DonationUser donationUser : donationUserList) {
            if (donationUser.getDonationStatus() == EXECUTION_BEFORE) {
                beforeCnt += 1;
            } else if (donationUser.getDonationStatus() == EXECUTION_UNDER) {
                underCnt += 1;
            } else if (donationUser.getDonationStatus() == EXECUTION_SUCCESS) {
                successCnt += 1;
            }
        }
        return DonationRes.DonationCount.builder()
                .beforeCnt(beforeCnt)
                .underCnt(underCnt)
                .successCnt(successCnt)
                .build();


    }

    public DonationRes.DonationList DonationList(DonationUser result) {
        String payDate="";
        if(result.getRegularPayment()!=null) {
            payDate = "매월 " + result.getRegularPayment().getPayDate() + "일 " + donationHelper.parsePriceComma(Math.toIntExact(result.getRegularPayment().getAmount()));
        }else{
            payDate = "단기 후원 " + result.getPrice() + "원";
        }

        return DonationRes.DonationList.builder()
                .donationId(result.getId())
                .donationDate(String.valueOf(result.getCreatedAt().getYear()).replace("20","")+"."+result.getCreatedAt().getMonthValue())
                .donationStatus(result.getDonationStatus().getName())
                .projectName(result.getProject().getProjectName())
                .regularStatus(result.getRegularStatus().getName())
                .regularDate(payDate)
                .build();
    }

    public DonationRes.DonationDetail getDonationDetail(DonationUser donationUser) {
        return DonationRes.DonationDetail
                .builder()
                .donationId(donationUser.getId())
                .userId(donationUser.getUserId())
                .name(donationUser.getUser().getName())
                .email(donationUser.getUser().getEmail())
                .phoneNumber(donationUser.getUser().getPhoneNumber())
                .amount(donationUser.getPrice())
                .inherenceName(donationUser.getInherenceName())
                .inherenceNumber(donationUser.getInherenceNumber())
                .payMethod(donationUser.getPayMethod().getValue())
                .donationStatus(donationUser.getDonationStatus())
                .regularStatus(donationUser.getRegularStatus().getName())
                .donationDate(donationUser.getCreatedAt().toString())
                .build();
    }

    public DonationHistory DonationHistory(Long id, HistoryStatus historyStatus, Long regularPaymentId) {
        return DonationHistory.builder()
                .donationUserId(id)
                .historyStatus(historyStatus)
                .regularPaymentId(regularPaymentId)
                .build();
    }

    public DonationRes.DonationRegular DonationRegular(RegularPayment regularPayment) {
        return DonationRes.DonationRegular
                .builder()
                .projectTitle(regularPayment.getProject().getProjectName())
                .imgUrl(regularPayment.getProject().getProjectImage().get(0).getUrl())
                .regularPayId(regularPayment.getId())
                .payDate(regularPayment.getPayDate())
                .amount(Math.toIntExact(regularPayment.getAmount()))
                .build();
    }

    public DonationRes.DonationRegularList DonationRegularList(DonationHistory result) {
        String histories = "";
        String flameImage = null;
        List<DonationRes.DonationHistoryImage> donationHistoryImages = new ArrayList<>();
        System.out.println("분기");
        if(result.getHistoryStatus() == HistoryStatus.CREATE){
            System.out.println("CREATE");
            histories = result.getDonationUser().getUser().getName() + "님의 불꽃이 탄생했습니다.";
            flameImage= result.getFlameImage();
        }else if(result.getHistoryStatus() == HistoryStatus.COMPLETE) {
            System.out.println("COMPLETE");
            histories = "'후원품'을 '후원처'에 전달했습니다.";
            donationHistoryImages = DonationHistoryImage(result.getHistoryImages());
        }else{
            histories = result.getCnt() + "명의 불꽃이 후원품으로 변했습니다.";
        }

        return DonationRes.DonationRegularList
                .builder()
                .historyId(result.getId())
                .historyDate(result.getCreatedAt().getYear()+"."+result.getCreatedAt().getMonthValue()+"."+result.getCreatedAt().getDayOfMonth())
                .histories(histories)
                .flameImage(flameImage)
                .historyStatus(result.getHistoryStatus())
                .donationHistoryImages(donationHistoryImages)
                .build();
    }

    private List<DonationRes.DonationHistoryImage> DonationHistoryImage(List<HistoryImage> historyImages) {
        List<DonationRes.DonationHistoryImage> donationHistoryImages = new ArrayList<>();

        System.out.println(historyImages.size());
        historyImages.forEach(
              result -> donationHistoryImages.add(
                        DonationRes.DonationHistoryImage
                                .builder()
                                .imageId(result.getId())
                                .imageUrl(result.getImgUrl())
                                .build()
                )
        );

        return donationHistoryImages;
    }

    public DonationRes.PayList PayList(DonationUser result) {
        String payStatus = "결제완료";
        if(result.getDonationStatus() == EXECUTION_REFUND){
            payStatus = "환불";
        }
        return DonationRes.PayList
                .builder()
                .payDate(donationHelper.timeFormat(result.getCreatedAt()))
                .payMethod(result.getPayMethod().getName())
                .payStatus(payStatus)
                .amount(donationHelper.parsePriceComma(Math.toIntExact(result.getPrice())))
                .build();
    }

    public DonationRes.BurningMatchRes BurningMatch(DonationUserRepository.flameList result) {
        List<String> imgUrlList = null;
        if(result.getImgUrlList()!=null){
            imgUrlList = Stream.of(result.getImgUrlList().split(",")).collect(Collectors.toList());
        }
        return DonationRes.BurningMatchRes
                .builder()
                .regularPayId(result.getRegularPayId())
                .projectId(result.getProjectId())
                .projectTitle(result.getProjectName())
                .imgUrl(result.getImgUrl())
                .totalDonationCnt(result.getTotalDonationCnt())
                .userProfileImages(imgUrlList)
                .build();
    }

    public DonationRes.FlameProjectList FlameProject(DonationUser result) {
        return DonationRes.FlameProjectList
                .builder()
                .donationId(result.getId())
                .projectId(result.getProjectId())
                .flameName(result.getInherenceName())
                .projectName(result.getProject().getProjectName())
                .imgUrl(result.getProject().getProjectImage().get(0).getUrl())
                .build();
    }

    public DonationRes.DonationFlame DonationFlame(RegularPayment regularPayment, DonationUser donationUser) {
        return DonationRes.DonationFlame
                .builder()
                .inherenceName(donationUser.getInherenceName())
                .regularPayStatus(regularPayment.getRegularPayStatus())
                .imgUrl(regularPayment.getProject().getProjectImage().get(0).getUrl())
                .regularPayId(regularPayment.getId())
                .payDate(regularPayment.getPayDate())
                .amount(Math.toIntExact(regularPayment.getAmount()))
                .build();
    }

    public ProjectRes.MatchHistory MatchHistory(DonationHistory result) {
        String histories = "";
        String profileImgUrl = "";
        String nickname = "";
        System.out.println(result.getHistoryStatus());
        if(result.getHistoryStatus().equals(HistoryStatus.CREATE)){
            histories = result.getDonationUser().getUser().getName() + "님의 불꽃이 탄생했습니다.";
            profileImgUrl = result.getDonationUser().getUser().getProfileImgUrl();
            nickname = result.getDonationUser().getUser().getNickname();
        }else if(result.getHistoryStatus().equals(HistoryStatus.COMPLETE)) {
            histories = "'후원품'을 '후원처'에 전달했습니다.";
            profileImgUrl = MATCH_PROFILE;
            nickname = MATCH_NAME;
        }else if(result.getHistoryStatus().equals(HistoryStatus.CHANGE)){
            histories = result.getCnt() + "명의 불꽃이 후원품으로 변했습니다.";
            profileImgUrl = MATCH_PROFILE;
            nickname = MATCH_NAME;
        }else if(result.getHistoryStatus().equals(HistoryStatus.TURN_ON)){
            histories = result.getDonationUser().getUser().getName() + "님이 매치를 켰습니다.";
            profileImgUrl = result.getDonationUser().getUser().getProfileImgUrl();
            nickname = result.getDonationUser().getUser().getNickname();
        }else if(result.getHistoryStatus().equals(HistoryStatus.START)){
            histories = "매치가 시작되었습니다.";
            profileImgUrl = MATCH_PROFILE;
            nickname = MATCH_NAME;
        }else if(result.getHistoryStatus().equals(HistoryStatus.FINISH)){
            histories = "매치가 종료되었습니다..";
            profileImgUrl = MATCH_PROFILE;
            nickname = MATCH_NAME;
        }


        return ProjectRes.MatchHistory
                .builder()
                .historyId(result.getId())
                .histories(histories)
                .profileImageUrl(profileImgUrl)
                .nickname(nickname)
                .historyStatus(result.getHistoryStatus())
                .historyDate(donationHelper.dayTimeFormat(result.getCreatedAt()))
                .build();
    }
}

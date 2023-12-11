package com.example.matchapi.donation.converter;

import com.example.matchapi.common.util.TimeHelper;
import com.example.matchapi.donation.dto.DonationReq;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.donation.helper.DonationHelper;
import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchcommon.annotation.Converter;
import com.example.matchdomain.donation.entity.*;
import com.example.matchdomain.donation.entity.enums.DonationStatus;
import com.example.matchdomain.donation.entity.enums.HistoryStatus;
import com.example.matchdomain.donation.entity.enums.PayMethod;
import com.example.matchdomain.donation.entity.enums.RegularStatus;
import com.example.matchdomain.donation.entity.flameEnum.FlameImage;
import com.example.matchdomain.donation.entity.flameEnum.FlameType;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.matchcommon.constants.MatchStatic.MATCH_NAME;
import static com.example.matchcommon.constants.MatchStatic.MATCH_PROFILE;
import static com.example.matchdomain.donation.entity.enums.DonationStatus.*;

@Converter
@RequiredArgsConstructor
public class DonationConverter {
    private final DonationHelper donationHelper;
    private final TimeHelper timeHelper;

    public DonationRes.DonationCount convertToDonationCount(List<DonationUser> donationUserList) { int beforeCnt=0;
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

    public DonationRes.DonationList convertToDonationListDetail(DonationUser result) {
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


    public DonationHistory convertToDonationHistory(Long id, HistoryStatus historyStatus) {
        return DonationHistory.builder()
                .donationUserId(id)
                .historyStatus(historyStatus)
                .build();
    }

    public DonationRes.DonationRegular convertToDonationRegular(RegularPayment regularPayment) {
        return DonationRes.DonationRegular
                .builder()
                .projectTitle(regularPayment.getProject().getProjectName())
                .imgUrl(regularPayment.getProject().getProjectImage().get(0).getUrl())
                .regularPayId(regularPayment.getId())
                .payDate(regularPayment.getPayDate())
                .amount(Math.toIntExact(regularPayment.getAmount()))
                .build();
    }

    public DonationRes.DonationRegularList convertToDonationRegularListDetail(DonationHistory result, String inherenceName) {
        String histories = "";
        String flameImage = null;
        List<DonationRes.DonationHistoryImage> donationHistoryImages = new ArrayList<>();
        if(result.getHistoryStatus() == HistoryStatus.CREATE){
            System.out.println("CREATE");
            histories = inherenceName + "가 생성되었습니다.";
            flameImage= result.getDonationUser().getFlameImage();
        }else if(result.getHistoryStatus() == HistoryStatus.COMPLETE) {
            System.out.println("COMPLETE");
            histories = "'후원품'을 '후원처'에 전달했습니다.";
            donationHistoryImages = convertToDonationHistoryImage(result.getHistoryImages());
        }else{
            histories = inherenceName + " 외 " + (result.getCnt()-1) + "마리의 불꽃이들이 '후원품'으로 변했습니다.";
        }

        return DonationRes.DonationRegularList
                .builder()
                .historyId(result.getId())
                .historyDate(timeHelper.matchTimeFormat(result.getCreatedAt()))
                .histories(histories)
                .flameImage(flameImage)
                .historyStatus(result.getHistoryStatus())
                .donationHistoryImages(donationHistoryImages)
                .build();
    }

    private List<DonationRes.DonationHistoryImage> convertToDonationHistoryImage(List<HistoryImage> historyImages) {
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

    public DonationRes.PayList convertToPayListDetail(DonationUser result) {
        String payStatus = "결제완료";
        if(result.getDonationStatus() == EXECUTION_REFUND){
            payStatus = "환불";
        }
        return DonationRes.PayList
                .builder()
                .payDate(timeHelper.timeFormat(result.getCreatedAt()))
                .payMethod(result.getPayMethod().getName())
                .payStatus(payStatus)
                .amount(donationHelper.parsePriceComma(Math.toIntExact(result.getPrice())))
                .inherenceNumber(result.getInherenceNumber())
                .build();
    }

    public DonationRes.BurningMatchRes convertToBurningMatchDetail(DonationUserRepository.flameList result) {
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

    public DonationRes.FlameProjectList convertToFlameProject(DonationUser result) {
        return DonationRes.FlameProjectList
                .builder()
                .donationId(result.getId())
                .projectId(result.getProjectId())
                .flameName(result.getInherenceName())
                .projectName(result.getProject().getProjectName())
                .imgUrl(result.getProject().getProjectImage().get(0).getUrl())
                .build();
    }

    public DonationRes.DonationFlame convertToDonationFlame(int sequence, DonationUser donationUser) {
        return DonationRes.DonationFlame
                .builder()
                .imgUrl(donationUser.getFlameImage())
                .flameType(donationUser.getFlameType().getType())
                .inherenceName(donationUser.getInherenceName())
                .usages(donationUser.getProject().getUsages())
                .amount(Math.toIntExact(donationUser.getPrice()))
                .sequence(sequence)
                .randomMessage(donationHelper.createRandomMessage(donationUser))
                .build();
    }

    public ProjectRes.MatchHistory convertToMatchHistoryDetail(DonationHistory result) {
        String histories = "";
        String profileImgUrl = "";
        String nickname = "";
        System.out.println(result.getHistoryStatus());
        if(result.getHistoryStatus().equals(HistoryStatus.CREATE)){
            histories = result.getDonationUser().getInherenceName() + "가 여기에 나타났습니다.";
            profileImgUrl = result.getDonationUser().getUser().getProfileImgUrl();
            nickname = result.getDonationUser().getUser().getNickname();
        }else if(result.getHistoryStatus().equals(HistoryStatus.COMPLETE)) {
            histories = "'후원품'을 " + result.getProject().getUsages() + "에 전달했습니다.";
            profileImgUrl = MATCH_PROFILE;
            nickname = MATCH_NAME;
        }else if(result.getHistoryStatus().equals(HistoryStatus.CHANGE)){
            histories = result.getCnt() + "마리의 불꽃이 후원품으로 변했습니다.";
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
            histories = "매치가 종료되었습니다.";
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
                .historyDate(timeHelper.dayTimeFormat(result.getCreatedAt()))
                .build();
    }

    public DonationHistory convertToDonationHistoryTurnOn(Long id, HistoryStatus historyStatus) {
        return DonationHistory.builder()
                .regularPaymentId(id)
                .historyStatus(historyStatus)
                .build();
    }

    public List<DonationRes.DonationList> convertToDonationList(Page<DonationUser> donationUsers) {
        List<DonationRes.DonationList> donationLists = new ArrayList<>();
        donationUsers.getContent().forEach(
                result ->{
                    donationLists.add(
                            convertToDonationListDetail(result)
                    );
                }
        );

        return donationLists;
    }

    public List<DonationRes.BurningMatchRes> BurningMatch(List<DonationUserRepository.flameList> flameLists) {
        List<DonationRes.BurningMatchRes> burningMatchRes = new ArrayList<>();
        flameLists.forEach(
                result -> {
                    burningMatchRes.add(convertToBurningMatchDetail(result));
                }
        );

        return burningMatchRes;
    }

    public List<DonationRes.DonationRegularList> convertToDonationRegularList(List<DonationHistory> donationHistories, String inherenceName){
        List<DonationRes.DonationRegularList> donationRegularLists = new ArrayList<>();
        donationHistories.forEach(
                result -> donationRegularLists.add(
                        convertToDonationRegularListDetail(result, inherenceName)
                )
        );
        return donationRegularLists;
    }

    public List<ProjectRes.MatchHistory> convertToMatchHistory(List<DonationHistory> donationHistories){
        List<ProjectRes.MatchHistory> matchHistories = new ArrayList<>();

        donationHistories.forEach(
                result -> matchHistories.add(
                        convertToMatchHistoryDetail(result)
                )
        );

        return matchHistories;
    }

    public List<DonationRes.FlameProjectList> convertToFlameProjectList(List<DonationUser> donationUsers){
        List<DonationRes.FlameProjectList> flameProjectLists = new ArrayList<>();

        donationUsers.forEach(
                result -> flameProjectLists.add(
                        convertToFlameProject(result)
                )
        );

        return flameProjectLists;
    }

    public List<DonationRes.PayList> convertToPayList(List<DonationUser> donationUsers) {
        List<DonationRes.PayList> payLists = new ArrayList<>();

        donationUsers.forEach(
                result -> payLists.add(
                        convertToPayListDetail(result)
                )
        );

        return payLists;
    }


    public DonationUser convertToTutorialDonation(User user, DonationReq.Tutorial tutorial, OrderRes.CreateInherenceDto createInherenceDto) {
        return DonationUser.builder()
                .userId(user.getId())
                .projectId(tutorial.getProjectId())
                .price(1L)
                .donationStatus(DonationStatus.EXECUTION_BEFORE)
                .payMethod(PayMethod.TUTORIAL)
                .inherenceName(createInherenceDto.getInherenceName())
                .inherenceNumber(createInherenceDto.getInherenceNumber())
                .regularStatus(RegularStatus.ONE_TIME)
                .flameImage(FlameImage.TUTORIAL_IMG.getImg())
                .flameType(FlameType.TUTORIAL_FLAME)
                .build();
    }

    public DonationRes.CompleteDonation convertToCompleteDonation(DonationUser donationUser, Project project) {
        return DonationRes.CompleteDonation
                .builder()
                .projectId(donationUser.getProjectId())
                .image(donationUser.getFlameImage())
                .inherenceName(donationUser.getInherenceName())
                .randomMessage(donationHelper.createRandomMessageTutorial(project.getProjectKind()))
                .build();
    }
}

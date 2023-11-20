package com.example.matchapi.donation.dto;

import com.example.matchdomain.donation.entity.enums.DonationStatus;
import com.example.matchdomain.donation.entity.enums.HistoryStatus;
import com.example.matchdomain.donation.entity.enums.RegularPayStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.List;

public class DonationRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationList {
        private Long donationId;

        private String donationDate;

        private String projectName;

        private String regularDate;

        private String donationStatus;

        private String regularStatus;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FlameList {
        private Long donationId;

        private String flameName;

        private String donationStatus;

        private Long amount;

        private String createdAt;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationCount {
        @Schema(description = "후원 집행 전 갯수", required = true, example = "후원 집행 전 갯수")
        private int beforeCnt;

        @Schema(description = "후원 집행 진행 중 갯수", required = true, example = "후원 집행 진행 중 갯수")
        private int underCnt;

        @Schema(description = "후원 집행 진행 중 갯수", required = true, example = "후원 집행 진행 중 갯수")
        private int successCnt;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationInfo {
        private String oneDayDonation;

        private String weekDonation;

        private String monthDonation;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationDetail {
        private Long donationId;

        private Long userId;

        private String name;

        private String email;

        private String phoneNumber;

        private Long amount;

        private String inherenceNumber;

        private String inherenceName;

        private String payMethod;

        private DonationStatus donationStatus;

        private String regularStatus;

        private String donationDate;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BurningMatchRes {
        @Schema(description = "후원 id", required = true, example = "2")
        private Long regularPayId;

        @Schema(description = "프로젝트 id", required = true, example = "2")
        private Long projectId;

        @Schema(description = "후원 관심 유무", required = true, example = "false")
        private boolean like;

        @Schema(description = "후원 이미지", required = true, example = "imgUrl")
        private String imgUrl;

        @Schema(description = "후원 타이틀", required = true, example = "후원 함께할 분, 들어와요")
        private String projectTitle;

        @Schema(description = "후원 유저 프로필 이미지 리스트", required = true, example = "")
        private List<String> userProfileImages;

        @Schema(description = "후원 유저 총원", required = true, example = "15")
        private int totalDonationCnt;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationRegular {
        @Schema(description = "후원 대표 이미지", required = true, example = "이미지 url")
        private String imgUrl;

        @Schema(description = "프로젝트 제목", required = true, example = "project 제목")
        private String projectTitle;

        @Schema(description = "후원 금액" , required = true, example = "후원 금액")
        private int amount;

        @Schema(description = "후원 id", required = true, example = "2")
        private Long regularPayId;

        @Schema(description = "후원 날짜", required = true, example = "2")
        private int payDate;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationFlame {
        @Schema(description = "불꽃이 이미지", required = true, example = "이미지 url")
        private String imgUrl;

        @Schema(description = "불꽃이 유형", required = true, example = "일반 불꽃")
        private String flameType;

        @Schema(description = "고유 이름", required = true, example = "불꽃이 고유 이름")
        private String inherenceName;

        @Schema(description = "성심수녀회")
        private String usages;

        @Schema(description = "후원 금액(전달된 온도)" , required = true, example = "1000")
        private int amount;

        @Schema(description = "생성 순서 0일시 단일 기부(즉 1회성 기부입니다) 생성 순서 표시 x", required = true, example = "2")
        private int sequence;


        private String randomMessage;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationRegularList {
        @Schema(description = "기록 id", required = true, example = "2")
        private Long historyId;

        @Schema(description = "후원 상태 CREATE = 불꽃이 생성, COMPLETE = 전달 완료, CHANGE = 후원품 변환", required = true, example = "CREATE")
        private HistoryStatus historyStatus;

        @Schema(description = "후원 기록", required = true, example = "N 명의 불꽃이 탄생")
        private String histories;

        @Schema(description = "후원 기록 날짜", required = true, example = "2023.12.25")
        private String historyDate;

        @Schema(description = "불꽃이 이미지", required = true, example = "이미지 url")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String flameImage;

        @Schema(description = "전달 사진 리스트 COMPLETE 인 경우에 이미지가 들어갑니다.", required = true, example = "")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<DonationHistoryImage> donationHistoryImages;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationHistoryImage {
        @Schema(description = "이미지 id", required = true, example = "2")
        private Long imageId;

        @Schema(description = "이미지 url", required = true, example = "이미지 url")
        private String imageUrl;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PayList {
        private String payDate;

        private String payStatus;

        private String payMethod;

        private String amount;

        private String inherenceNumber;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FlameProject {
        private int totalCnt;

        private List<FlameProjectList> flameProjectLists;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FlameProjectList {
        private Long donationId;

        private Long projectId;

        private String flameName;

        private String projectName;

        private String imgUrl;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MatchList {
        private String projectTitle;

        private String regularDate;

        private RegularPayStatus regularPayStatus;

        private int payDate;

        private String amount;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BurningFlameDto implements Serializable {
        @Schema(description = "프로젝트 id", example = "1")
        private Long projectId;

        @Schema(description = "불꽃이 id 상세조회시 필요", required = true, example = "2")
        private Long donationId;

        @Schema(description = "후원처 명")
        private String usages;

        @Schema(description = "고유 불꽃이 이름")
        private String inherenceName;

        @Schema(description = "불꽃이 이미지")
        private String image;

        @Schema(description = "랜덤 불꽃이 메세지")
        private String randomMessage;
    }


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectDonationStatus {
        @Schema(description = "프로젝트 id", example = "1")
        private Long projectId;

        @Schema(description = "후원처 명")
        private String usages;

        @Schema(description = "분류 대기 금액")
        private int waitingSortingAmount;

        @Schema(description = "수입 금액")
        private int importedAmount;

        @Schema(description = "지출 완료 금액")
        private int completeAmount;
    }
}

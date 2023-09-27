package com.example.matchapi.donation.dto;

import com.example.matchdomain.donation.entity.DonationStatus;
import com.example.matchdomain.donation.entity.HistoryStatus;
import com.example.matchdomain.donation.entity.RegularPayStatus;
import com.example.matchdomain.donation.entity.RegularStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
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
        @Schema(description = "후원 대표 이미지", required = true, example = "이미지 url")
        private String imgUrl;

        @Schema(description = "고유 이름", required = true, example = "불꽃이 고유 이름")
        private String inherenceName;

        @Schema(description = "PROCEEDING = 기부 진행중, PROJECT_FINISH = 프로젝트 마감으로 인한 매칭 종료, USER_CANCEL = 유저 후원 취소")
        private RegularPayStatus regularPayStatus;

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
        private String flameImage;

        @Schema(description = "전달 사진 리스트 COMPLETE 인 경우에 이미지가 들어갑니다.", required = true, example = "")
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
}

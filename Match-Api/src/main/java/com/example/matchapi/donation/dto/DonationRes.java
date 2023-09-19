package com.example.matchapi.donation.dto;

import com.example.matchdomain.donation.entity.DonationStatus;
import com.example.matchdomain.donation.entity.HistoryStatus;
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
        private int amount;

        private Long regularPayId;

        private int payDate;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationRegularList {
        private Long historyId;

        private HistoryStatus historyStatus;

        private String histories;

        private String historyDate;

        private String flameImage;

        private List<DonationHistoryImage> donationHistoryImages;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationHistoryImage {
        private Long imageId;

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
}

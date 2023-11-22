package com.example.matchapi.project.dto;

import com.example.matchdomain.donation.entity.enums.HistoryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ProjectList {
        @Schema(description = "projectId 값", required = true, example = "1")
        private Long projectId;
        @Schema(description = "프로젝트 대표 이미 URL", required = true, example = "imgUrl")
        private String imgUrl;
        @Schema(description = "프로젝트 이름", required = true, example = "title")
        private String title;
        @Schema(description = "프로젝트 사용처", required = true, example = "프로젝트 사용처")
        private String usages;
        @Schema(description = "프로젝트 후원 분야",required = true, example = "kind")
        private String kind;
        @Schema(description = "프로젝트 관심 유무", required = true, example = "true")
        private boolean like;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectLists {
        @Schema(description = "projectId 값", required = true, example = "1")
        private Long projectId;
        @Schema(description = "프로젝트 대표 이미지 URL", required = true, example = "imgUrl")
        private String imgUrl;
        @Schema(description = "프로젝트 이름", required = true, example = "title")
        private String title;
        @Schema(description = "프로젝트 사용처", required = true, example = "프로젝트 사용처")
        private String usages;
        @Schema(description = "프로젝트 후원 분야",required = true, example = "kind")
        private String kind;
        @Schema(description = "프로젝트 관심 유무", required = true, example = "true")
        private boolean like;
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
    public static class ProjectDetail {
        @Schema(description = "projectId 값", required = true, example = "1")
        private Long projectId;
        @Schema(description = "프로젝트 이미지 리스트", required = true, example = "imgUrl")
        private List<ProjectImgList> projectImgList;
        @Schema(description = "프로젝트 이름", required = true, example = "title")
        private String title;
        @Schema(description = "프로젝트 사용처", required = true, example = "프로젝트 사용처")
        private String usages;
        @Schema(description = "기부 가능 유무", required = true, example = "true")
        private boolean donationAble;
        @Schema(description = "프로젝트 후원 분야",required = true, example = "kind")
        private String kind;
        @Schema(description = "프로젝트 정기 후원인지 아닌지", required = true, example = "REGULAR OR ONE_TIME")
        private String regularStatus;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectImgList{
        @Schema(description = "imageId 값", required = true, example = "1")
        private Long imgId;

        @Schema(description = "이미지 URL", required = true, example = "imgUrl")
        private String imgUrl;

        @Schema(description = "이미지 순서", required = true, example = "1")
        private int sequence;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CommentList {
        private Long commentId;

        private String comment;

        @JsonFormat(pattern = "yyyy.MM.dd HH.mm")
        private LocalDateTime commentDate;

        @Schema(description = "이미지 URL", required = true, example = "imgUrl")
        private String profileImgUrl;

        private String nickname;

        private Long userId;

        private boolean isMy;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectAdminList {
        private Long projectId;

        private String projectName;

        private String usages;

        private int totalDonationCnt;

        private int totalAmount;

        private String projectStatus;

        private String regularStatus;

        private String status;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectAdminDetail {
        private Long projectId;

        private String projectName;

        private String detail;

        private String usages;

        private String startDate;

        private String endDate;

        private String projectStatus;

        private String regularStatus;

        private String status;

        private String searchKeyword;

        private int totalAmount;

        private int totalDonationCnt;

        private int regularDonationCnt;

        private List<ProjectImgList> projectImgLists;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationList {
        private Long donationId;

        private Long userId;

        private String name;

        private String email;

        private String phoneNumber;

        private Long amount;

        private String inherenceNumber;

        private String inherenceName;

        private String payMethod;

        private String donationStatus;

        private String donationStatusValue;

        private String regularStatus;

        private String donationDate;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PatchProjectImg {
        private Long projectImg;

        private String imgUrl;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectLike {
        @Schema(description = "프로젝트 관심 유무", required = true, example = "true")
        private boolean like;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectAppDetail {
        @Schema(description = "projectId 값", required = true, example = "1")
        private Long projectId;
        @Schema(description = "썸네일 이미지")
        String thumbNail;
        @Schema(description = "프로젝트 이미지 리스트", required = true, example = "imgUrl")
        private List<ProjectImgList> projectImgList;
        @Schema(description = "프로젝트 이름", required = true, example = "title")
        private String title;
        @Schema(description = "프로젝트 사용처", required = true, example = "프로젝트 사용처")
        private String usages;
        @Schema(description = "프로젝트 후원 분야",required = true, example = "kind")
        private String kind;
        @Schema(description = "프로젝트 정기 후원인지 아닌지", required = true, example = "REGULAR OR ONE_TIME")
        private String regularStatus;
        private boolean like;
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
    public static class MatchHistory {
        @Schema(description = "기록 id", required = true, example = "2")
        private Long historyId;

        @Schema(description = "후원 상태 START = 매치 시작 FINISH = 매치 종료 CREATE = 불꽃이 생성, COMPLETE = 전달 완료, CHANGE = 후원품 변환, TURN_ON 정기 후원 시작", required = true, example = "CREATE")
        private HistoryStatus historyStatus;

        @Schema(description = "후원 기록", required = true, example = "N 명의 불꽃이 탄생")
        private String histories;

        @Schema(description = "후원 기록 날짜", required = true, example = "2023.12.25 07:23")
        private String historyDate;

        @Schema(description = "프로필 이미지", required = true, example = "이미지 url")
        private String profileImageUrl;

        private String nickname;

    }
}

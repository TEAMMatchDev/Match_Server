package com.example.matchapi.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class ProjectRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
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
        @Schema(description = "프로젝트 관 유무", required = true, example = "true")
        private boolean like;
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

        private String commentDate;

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

        private String regularStatus;

        private String donationDate;
    }
}

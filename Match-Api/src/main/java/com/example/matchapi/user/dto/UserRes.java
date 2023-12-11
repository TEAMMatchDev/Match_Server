package com.example.matchapi.user.dto;

import com.example.matchdomain.donation.entity.enums.DonationStatus;
import com.example.matchdomain.user.entity.enums.Alarm;
import com.example.matchdomain.user.entity.enums.SocialType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

public class UserRes {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description ="01-02,03🔑 로그인 후 토큰 발급 API Response")
    public static class UserToken{
        @Schema(description = "userId 값", required = true, example = "1")
        private Long userId;
        @Schema(description = "액세스 토큰", required = true, example = "asdkjanwjkldnjk----")
        private String accessToken;
        @Schema(description = "리프레쉬 토큰", required = true, example = "asdkjanwjkldnjk----")
        private String refreshToken;
        @Schema(description = "회원가입 유무 true - 회원가입, false - 로그인")
        private boolean isNew;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Token{
        private String accessToken;

        private String refreshToken;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserAddress {
        private Long id;
        private Long userId;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description ="01-04🔑 문자인증 API Response")
    public static class Sms {
        @Schema(description = "회원가입 인증 번호", required = true, example = "241244")
        private String number;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description ="02-02👤 유저 정보 편집 API Response")
    public static class EditMyPage {
        @Schema(description = "userId", required = true, example = "userId 값")
        private Long userId;
        @Schema(description = "유저 이메일", required = true, example = "match123@gmail.com")
        private String email;
        @Schema(description = "유저 이름", required = true, example = "임현우")
        private String name;
        @Schema(description = "유저 전화번호", required = true, example = "01041231434")
        private String phone;
        @Schema(description = "유저 성별", required = true, example = "남자, 여자, 알 수 없음")
        private String gender;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @Schema(description = "유저 생일", required = true, example = "19990413")
        private String birthDate;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description ="02-01👤 유저 정보 조회 API Response")
    public static class MyPage {
        @Schema(description = "닉네임")
        private String name;

        @Schema(description = "진행중 매치 갯수", required = true, example = "후원 집행 진행 중 갯수")
        private int underCnt;

        @Schema(description = "종료된 매치 갯수", required = true, example = "후원 집행 진행 중 갯수")
        private int successCnt;

        @Schema(description = "좋아하는 매치", required = true, example = "후원 집행 전 갯수")
        private int likeCnt;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "토큰 재발급", name = "ReIssueToken")
    public static class ReIssueToken {
        private String accessToken;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpInfo {
        private Long totalUserCnt;

        private Long oneDayUserCnt;

        private Long weekUserCnt;

        private Long monthUserCnt;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserList {
        private Long userId;

        private String name;

        private String birth;

        private String socialType;

        private String gender;

        private String phoneNumber;

        private String email;

        private boolean card;

        private int donationCnt;

        private int totalAmount;

        private String status;

        private String createdAt;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserAdminDetail {
        private Long userId;

        private String name;

        private String birth;

        private String socialType;

        private String gender;

        private String phoneNumber;

        private String email;

        private boolean card;

        private int donationCnt;

        private int totalAmount;

        private String status;

        private String createdAt;
    }

    public static class EmailAuth {
        private String number;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Profile {
        private String profileImgUrl;

        private String name;

        private String nickName;

        private SocialType socialType;

        private String email;;

        private String phone;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AlarmAgreeList {
        private Alarm serviceAlarm;

        private Alarm eventAlarm;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserFlameListDto {
        private Long donationId;

        private int donationCnt;

        private String inherenceName;

        private String inherenceNumber;

        private DonationStatus donationStatus;

        private String donationStatusName;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDelete {
        private Long userId;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDonationInfo {
        private int nowDonationCnt;

        private int pastDonationCnt;

        private int totalCnt;

        private List<UserFlameListDto> flameLists;
    }


}

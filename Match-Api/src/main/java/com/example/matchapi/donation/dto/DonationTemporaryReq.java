package com.example.matchapi.donation.dto;

import com.example.matchcommon.annotation.Enum;
import com.example.matchdomain.donationTemporary.entity.AlarmMethod;
import com.example.matchdomain.donationTemporary.entity.DonationKind;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DonationTemporaryReq {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationInfo {
        @NotNull(message = "유저 이름을 입력해주세요")
        private String username;

        private String phoneNumber;

        private String email;

        @Enum(message = "KAKAO, SMS, EMAIL 중 입력해주세요")
        private AlarmMethod alarmMethod;

        @Enum(message = "DOG, CHILD, OCEAN, VISUALLY_IMPAIRED 중 입력해주세요.")
        private DonationKind donationKind;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DonationDeposit {
        private Long donationRequestId;

        @NotEmpty(message = "이름을 입력해주새요.")
        private String name;

        @NotNull(message = "가격을 입력해주세요.")
        private int amount;
    }
}

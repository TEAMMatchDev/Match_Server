package com.example.matchinfrastructure.oauth.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserInfoDto {

    private Properties properties;
    private String id;

    private KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Properties {
        private String nickname;
    }

    @Getter
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KakaoAccount {

        private Profile profile;
        private String email;
        private String phoneNumber;
        private Boolean genderNeedsAgreement;
        private String gender;
        private Boolean birthyearNeedsAgreement;
        private String birthyear;
        private Boolean birthdayNeedsAgreement;
        private String birthday;
        private String name;

        @Getter
        @NoArgsConstructor
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Profile {
            private String profileImageUrl;
        }

        public String getProfileImageUrl() {
            return profile.getProfileImageUrl();
        }

    }

    public String getId() {
        return id;
    }

    public String getGender(){
        return kakaoAccount.getGender();
    }
    public String getBirthYear(){
        return kakaoAccount.getBirthyear();
    }
    public String getBirthDay(){
        return kakaoAccount.getBirthday();
    }

    public String getEmail() {
        return kakaoAccount.getEmail();
    }

    public String getPhoneNumber() {
        return kakaoAccount.getPhoneNumber();
    }

    public String getName() {
        return kakaoAccount.getName() != null ? kakaoAccount.getName() : properties.getNickname();
    }

    public String getProfileUrl() {
        return kakaoAccount.getProfileImageUrl();
    }
}
package com.example.matchapi.user.dto;


import com.example.matchdomain.user.entity.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import com.example.matchcommon.annotation.Enum;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.constraints.*;
import java.time.LocalDate;

public class UserReq {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Schema(description = "01-02,03🔑 소셜 로그인 토큰 API Request")
    public static class SocialLoginToken{
        @Schema(description = "소셜 액세스 토큰", required = true, example = "asdkjanwjkldnjk----")
        @NotEmpty(message = "토큰을 입력해주세요")
        private String accessToken;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description ="01-04🔑 문자인증 API Request")
    public static class Sms {
        @Schema(description = "전화번호 입력", required = true, example = "01012345678")
        @NotEmpty(message = "전화번호를 입력해주세요")
        @Size(min = 11, max = 11, message = "전화번호는 11자리 이어야 합니다.")
        @Pattern(regexp = "^01(?:0|1|[6-9])[0-9]{7,8}$", message = "전화번호 형식에 맞지 않습니다. 01012345678 '-' 를 제외하고 입력해주세요. ")
        private String phone;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "01-05🔑 회원가입 API Request")
    public static class SignUpUser {
        @Email
        @NotEmpty (message = "이메일을 입력해주세요")
        @Schema(description ="이메일",required = true,example = "match123@gmail.com")
        private String email;
        @NotEmpty (message = "비밀번호를 입력해주세요")
        @Schema(description ="비밀번호",required = true,example = "1234")
        private String password;
        @NotEmpty (message = "이름을 입력해주세요")
        @Schema(description ="이름",required = true,example = "match123")
        private String name;
        @NotEmpty(message = "전화번호를 입력해주세요")
        @Size(min = 11, max = 11, message = "전화번호는 11자리 이어야 합니다.")
        @Pattern(regexp = "^01(?:0|1|[6-9])[0-9]{7,8}$", message = "전화번호 형식에 맞지 않습니다. 01012345678 '-' 를 제외하고 입력해주세요. ")
        @Schema(description ="전화번호",required = true,example = "0101234567")
        private String phone;
        @Schema(description ="성별",required = true,example = "남성은 남성 여성은 여성 선택 안함 ")
        @Enum(message="남성, 여성, 선택 안함에 맞춰서 입력해주세요")
        private Gender gender;
        @NotEmpty (message = "생년월일을 입력해주세요")
        @Schema(description ="생일",required = true,example = "20200101")
        private String birthDate;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "01-05🔑 회원가입 핸드폰 번호 검증 API Request")
    public static class UserPhone {
        @NotEmpty(message = "전화번호를 입력해주세요")
        @Size(min = 11, max = 11, message = "전화번호는 11자리 이어야 합니다.")
        @Pattern(regexp = "^01(?:0|1|[6-9])[0-9]{7,8}$", message = "전화번호 형식에 맞지 않습니다. 01012345678 '-' 를 제외하고 입력해주세요. ")
        @Schema(description ="전화번호",required = true,example = "0101234567")
        private String phone;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "01-05-01🔑 회원가입 이메일 검증 API Request")
    public static class UserEmail {
        @Email
        @Schema(description ="이메일", required = true,example = "match123@gmail.com")
        private String email;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "01-06🔑 로그인 API Request")
    public static class LogIn {
        @Email
        @Schema(description ="이메일",required = true,example = "match123@gmail.com")
        private String email;
        @NotEmpty (message = "비밀번호를 입력해주세요")
        @Schema(description ="비밀번호",required = true,example = "1234")
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EditMyPage {
        private String orgPassword;

        private String newPassword;

    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserEmailAuth {
        private String email;

        private String code;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserPhoneAuth {
        private String phone;

        private String code;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModifyProfile {
        @Schema(description = "이름", required = false, example = "이메누")
        private String name;

        @Schema(description="프로필 사진 변경",required =false,example = "프로필 사진 변경")
        private MultipartFile multipartFile;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FcmToken {
        private String fcmToken;

        private String deviceId;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModifyPhone {
        private String oldPhone;

        private String newPhone;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ModifyEmail {
        private String oldEmail;

        private String newEmail;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AppleUserInfo {
        @NotBlank(message = "이름을 입력해주세요")
        private String name;

        @NotBlank(message = "생일을 입력해주세요")
        private LocalDate birthDate;

        @NotBlank(message = "전화번호를 입력해주세요")
        private String phone;
    }


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AppleCode {
        @Schema(description = "애플 코드 입력", required = true)
        @NotBlank(message = "코드를 입력해주세요")
        private String code;
    }
}

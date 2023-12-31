package com.example.matchapi.user.helper;

import com.example.matchcommon.annotation.Helper;
import com.example.matchcommon.exception.BaseDynamicException;
import com.example.matchdomain.user.entity.enums.Gender;
import com.example.matchdomain.user.entity.enums.SocialType;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.format.DateTimeFormatter;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static com.example.matchdomain.common.model.Status.ACTIVE;
import static com.example.matchdomain.user.exception.UserSignUpErrorCode.EXIST_USER_PHONENUMBER;
import static com.example.matchdomain.user.entity.enums.SocialType.*;

@Helper
@RequiredArgsConstructor
public class AuthHelper {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public String createRandomPassword() {
        return passwordEncoder.encode(UUID.randomUUID().toString());
    }


    public LocalDate birthConversion(String birthYear, String birthDay) {
        if (birthYear != null && birthDay != null) {
            String year = String.valueOf(birthYear);
            birthDay = birthDay.replace("-", "");
            String date = birthDay.substring(0, 2) + "-" + birthDay.substring(2, 4);
            String dateString = year + "-" + date;
            return LocalDate.parse(dateString);
        } else {
            System.out.println(false);
            return null;
        }
    }

    public void checkUserExists(String phoneNumber, SocialType socialType) {
        HashMap<String, String> errorType = new HashMap<>();
        Optional<User> user = userRepository.findByPhoneNumberAndSocialTypeNotAndStatus(phoneNumber.replaceAll("\\D+", "").replaceFirst("^82", "0"), socialType, ACTIVE);

        if (user.isPresent()) {
            errorType.put("signUpType", socialTypeConversion(user.get().getSocialType()));
            throw new BaseDynamicException(EXIST_USER_PHONENUMBER, errorType);
        }
    }

    public LocalDate birthConversionToLocalDate(String birthDate) {
        DateTimeFormatter sourceFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(birthDate, sourceFormatter);
    }

    public boolean checkGuest(User user) {
        return user != null;
    }

    @FunctionalInterface
    private interface GenderResolver {
        Gender resolveGender(String gender);
    }

    @FunctionalInterface
    private interface SocialTypeResolver {
        String resolveSocialType(String social);
    }


    public String socialTypeConversion(SocialType socialType) {
        SocialTypeResolver resolver = social -> {
            if (social.equalsIgnoreCase(NAVER.getValue())) {
                return "이미 가입된 네이버 계정이 존재 합니다. 네이버 계정으로 로그인 해주세요.";
            } else if (social.equalsIgnoreCase(KAKAO.getValue())) {
                return "이미 가입된 카카오 계정이 존재 합니다. 카카오 계정으로 로그인 해주세요.";
            } else if (social.equalsIgnoreCase(NORMAL.getValue())) {
                return "이미 가입된 일반 계정이 존재 합니다. 일반 계정으로 로그인 해주세요.";
            } else {
                return "";
            }
        };

        return resolver.resolveSocialType(socialType.getValue());
    }

    public Gender genderConversion(String authGender) {

        GenderResolver resolver = gender -> {
            if (gender == null) {
                return Gender.UNKNOWN;
            } else if (gender.equalsIgnoreCase("female") || gender.equalsIgnoreCase("F") || gender.equalsIgnoreCase("여자")) {
                return Gender.FEMALE;
            } else if (gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("남자")) {
                return Gender.MALE;
            } else {
                return Gender.UNKNOWN;
            }
        };

        return resolver.resolveGender(authGender);
    }
}

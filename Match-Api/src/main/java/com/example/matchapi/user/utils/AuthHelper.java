package com.example.matchapi.user.utils;

import com.example.matchcommon.annotation.Helper;
import com.example.matchdomain.user.entity.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.UUID;

@Helper
@RequiredArgsConstructor
public class AuthHelper {
    private final PasswordEncoder passwordEncoder;

    public String createRandomPassword() {
        return passwordEncoder.encode(UUID.randomUUID().toString());
    }

    public Gender genderConversion(String authGender){

        GenderResolver resolver = gender -> {
            if (gender.isEmpty()){
                return Gender.UNKNOWN;
            } else if (gender.equalsIgnoreCase("female")||gender.equalsIgnoreCase("F")) {
                return Gender.FEMALE;
            } else if (gender.equalsIgnoreCase("male")||gender.equalsIgnoreCase("M")) {
                return Gender.MALE;
            }else{
                return Gender.UNKNOWN;
            }
        };

        return resolver.resolveGender(authGender);
    }

    public LocalDate birthConversion(String birthYear, String birthDay, Boolean birthdayNeedsAgreement) {
        if(birthdayNeedsAgreement) {
            String year = String.valueOf(birthYear);
            String date = birthDay.substring(0, 2) + "-" + birthDay.substring(2, 4);
            String dateString = year + "-" + date;
            return LocalDate.parse(dateString);
        }else{
            return null;
        }
    }

    @FunctionalInterface
    private interface GenderResolver {
        Gender resolveGender(String gender);
    }

}

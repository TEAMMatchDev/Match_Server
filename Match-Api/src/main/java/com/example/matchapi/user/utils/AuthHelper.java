package com.example.matchapi.user.utils;

import com.example.matchcommon.annotation.Helper;
import com.example.matchcommon.exception.BaseException;
import com.example.matchcommon.properties.CoolSmsProperties;
import com.example.matchdomain.user.entity.Gender;
import com.example.matchdomain.user.entity.SocialType;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static com.example.matchcommon.exception.CommonResponseStatus.EXIST_USER_PHONENUMBER;
import static com.example.matchdomain.user.entity.SocialType.*;

@Helper
@RequiredArgsConstructor
public class AuthHelper {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CoolSmsProperties coolSmsProperties;

    public String createRandomPassword() {
        return passwordEncoder.encode(UUID.randomUUID().toString());
    }


    public LocalDate birthConversion(String birthYear, String birthDay) {
        if(birthYear!=null && birthDay!=null) {
            String year = String.valueOf(birthYear);
            birthDay = birthDay.replace("-", "");
            String date = birthDay.substring(0, 2) + "-" + birthDay.substring(2, 4);
            String dateString = year + "-" + date;
            return LocalDate.parse(dateString);
        }else{
            System.out.println(false);
            return null;
        }
    }

    public void checkUserExists(String phoneNumber, SocialType socialType) {
        HashMap<String,String> errorType = new HashMap<>();
        Optional<User> user = userRepository.findByPhoneNumberAndSocialTypeNot(phoneNumber.replaceAll("\\D+", "").replaceFirst("^82", "0"),socialType);

        if(user.isPresent()){
            errorType.put("signUpType",socialTypeConversion(user.get().getSocialType()));
            throw new BaseException(EXIST_USER_PHONENUMBER, errorType);
        }
    }

    public String createRandomNumber() {
        Random rand  = new Random();
        String numStr = "";
        for(int i=0; i<6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr+=ran;
        }

        return numStr;
    }

    /*
    public void sendSms(String phone, String number) throws CoolsmsException {
        String api_key = coolSmsProperties.getApi(); // 발급받은 api_key
        String api_secret = coolSmsProperties.getSecret(); // 발급받은 api_secret
        Message coolsms = new Message(api_key, api_secret);



        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to",phone);    // 수신전화번호 (ajax로 view 화면에서 받아온 값으로 넘김)
        params.put("from", coolSmsProperties.getSender());    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "sms");
        params.put("text", "인증번호는 [" + number + "] 입니다.");

        coolsms.send(params); // 메시지 전송

    }

     */

    @FunctionalInterface
    private interface GenderResolver {
        Gender resolveGender(String gender);
    }

    @FunctionalInterface
    private interface SocialTypeResolver {
        String resolveSocialType(String social);
    }


    public String socialTypeConversion(SocialType socialType){
        SocialTypeResolver resolver = social -> {
            if (social.equalsIgnoreCase(NAVER.getValue())){
                return "이미 가입된 네이버 계정이 존재 합니다. 네이버 계정으로 로그인 해주세요.";
            } else if (social.equalsIgnoreCase(KAKAO.getValue())) {
                return "이미 가입된 카카오 계정이 존재 합니다. 카카오 계정으로 로그인 해주세요.";
            } else if (social.equalsIgnoreCase(NORMAL.getValue())) {
                return "이미 가입된 일반 계정이 존재 합니다. 일반 계정으로 로그인 해주세요.";
            }else{
                return "";
            }
        };

        return resolver.resolveSocialType(socialType.getValue());
    }

    public Gender genderConversion(String authGender){

        GenderResolver resolver = gender -> {
            if (gender==null){
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
}

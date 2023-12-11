package com.example.matchapi.user.service;

import com.example.matchapi.common.model.AlarmType;
import com.example.matchapi.donation.service.DonationService;
import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.user.converter.UserConverter;
import com.example.matchapi.user.dto.UserReq;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchcommon.annotation.RedissonLock;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.user.adaptor.UserAdaptor;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.enums.Alarm;
import com.example.matchdomain.user.exception.ModifyEmailCode;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import com.example.matchinfrastructure.oauth.apple.service.AppleAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.example.matchapi.common.model.AlarmType.EVENT;
import static com.example.matchcommon.constants.MatchStatic.*;
import static com.example.matchdomain.user.entity.enums.Alarm.ACTIVE;
import static com.example.matchdomain.user.entity.enums.Alarm.INACTIVE;
import static com.example.matchdomain.user.exception.ModifyEmailCode.NOT_CORRECT_EMAIL;
import static com.example.matchdomain.user.exception.ModifyPhoneErrorCode.NOT_CORRECT_PHONE;
import static com.example.matchdomain.user.exception.UserNormalSignUpErrorCode.USERS_EXISTS_PHONE;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserConverter userConverter;
    private final S3UploadService s3UploadService;
    private final DonationService donationService;
    private final AppleAuthService appleAuthService;
    private final AuthService authService;
    private final UserAdaptor userAdaptor;

    public UserRes.EditMyPage getEditMyPage(User user) {
        return userConverter.convertToMyPage(user);
    }


    public OrderRes.UserDetail getUserInfo(User user) {
        return userConverter.convertToUserInfo(user);
    }


    public UserRes.Profile getProfile(User user) {
        return userConverter.convertToUserProfile(user);
    }

    @Transactional
    @CacheEvict(value = "userCache", key = "#user.id", cacheManager = "redisCacheManager")
    public void modifyUserProfile(User user, UserReq.ModifyProfile modifyProfile) {
        if(modifyProfile.getName() == null && modifyProfile.getMultipartFile()!=null){
            String beforeProfileImg = user.getProfileImgUrl();
            if(!beforeProfileImg.equals(BASE_PROFILE)){
                s3UploadService.deleteFile(beforeProfileImg);
            }
            String newProfileImg = s3UploadService.uploadProfilePresentFile(user.getId(), modifyProfile.getMultipartFile());
            user.setProfileImgUrl(newProfileImg);
        }
        else if(modifyProfile.getMultipartFile() == null && modifyProfile.getName()!=null){
            System.out.println("유저 닉네임 편집");
            user.setNickname(modifyProfile.getName());
        }
        else if (modifyProfile.getMultipartFile() != null){
            String beforeProfileImg = user.getProfileImgUrl();
            if(!beforeProfileImg.equals(BASE_PROFILE)){
                s3UploadService.deleteFile(beforeProfileImg);
            }
            String newProfileImg = s3UploadService.uploadProfilePresentFile(user.getId(), modifyProfile.getMultipartFile());
            user.setModifyProfile(newProfileImg, modifyProfile.getName());
        }

        userAdaptor.save(user);
    }

    @Transactional
    public void modifyPhoneNumber(User user, UserReq.ModifyPhone phone) {
        if(!user.getPhoneNumber().equals(phone.getOldPhone())) throw new BadRequestException(NOT_CORRECT_PHONE);
        if(userAdaptor.existsPhoneNumber(phone.getNewPhone())) throw new BadRequestException(USERS_EXISTS_PHONE);
        user.setPhoneNumber(phone.getNewPhone());
        userAdaptor.save(user);
    }

    @Transactional
    public void modifyEmail(User user, UserReq.ModifyEmail email) {
        if(!user.getEmail().equals(email.getOldEmail())) throw new BadRequestException(NOT_CORRECT_EMAIL);
        if(userAdaptor.existsEmail(email.getNewEmail())) throw new BadRequestException(ModifyEmailCode.USERS_EXISTS_EMAIL);
        user.setEmail(email.getNewEmail());
        userAdaptor.save(user);
    }

    public UserRes.AlarmAgreeList getAlarmAgreeList(User user) {
        System.out.println(user.getName());
        return userConverter.convertToAlarmAgree(user);
    }

    @CacheEvict(value = "userCache", key = "#user.id", cacheManager = "redisCacheManager")
    public UserRes.AlarmAgreeList patchAlarm(User user, AlarmType alarmType) {
        if(alarmType.equals(EVENT)){
            Alarm alarm = user.getEventAlarm();
            if(alarm == ACTIVE){
                user.setEventAlarm(INACTIVE);
            }
            else{
                user.setEventAlarm(ACTIVE);
            }
        }else{
            Alarm alarm = user.getServiceAlarm();
            if(alarm == ACTIVE){
                user.setServiceAlarm(INACTIVE);
            }
            else{
                user.setServiceAlarm(ACTIVE);
            }
        }

        user = userAdaptor.save(user);

        return userConverter.convertToAlarmAgree(user);
    }

    @Transactional
    public void postAppleUserInfo(User user, UserReq.AppleUserInfo appleUserInfo) {
        authService.checkUserPhone(new UserReq.UserPhone(appleUserInfo.getPhone()));
        user.updateUserInfo(appleUserInfo.getBirthDate(), appleUserInfo.getName(), appleUserInfo.getPhone());

        userAdaptor.save(user);
    }

    @RedissonLock(LockName = "유저 탈퇴", key = "#user.id")
    public void deleteUserInfo(User user) {
        user.setStatus(Status.INACTIVE);
        donationService.deleteRegularPayment(user);
        userAdaptor.save(user);
    }

    public void deleteAppleUserInfo(User user, UserReq.AppleCode appleCode) {
        appleAuthService.revokeUser(appleCode.getCode());
        deleteUserInfo(user);
    }

    public User findByUser(String userId) {
        return userAdaptor.findByUser(userId);
    }

}

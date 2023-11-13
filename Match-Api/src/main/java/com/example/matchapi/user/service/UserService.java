package com.example.matchapi.user.service;

import com.example.matchapi.common.model.AlarmType;
import com.example.matchapi.donation.service.DonationService;
import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.order.service.OrderService;
import com.example.matchapi.project.converter.ProjectConverter;
import com.example.matchapi.project.helper.ProjectHelper;
import com.example.matchapi.user.converter.UserConverter;
import com.example.matchapi.user.dto.UserReq;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchcommon.annotation.RedissonLock;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.donation.repository.RegularPaymentRepository;
import com.example.matchdomain.project.repository.ProjectUserAttentionRepository;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.UserAddress;
import com.example.matchdomain.user.entity.enums.Alarm;
import com.example.matchdomain.user.entity.pk.UserFcmPk;
import com.example.matchdomain.user.exception.ModifyEmailCode;
import com.example.matchdomain.user.repository.UserAddressRepository;
import com.example.matchdomain.user.repository.UserFcmTokenRepository;
import com.example.matchdomain.user.repository.UserRepository;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final UserConverter userConverter;
    private final ProjectConverter projectConverter;
    private final ProjectUserAttentionRepository projectUserAttentionRepository;
    private final OrderService orderService;
    private final RegularPaymentRepository regularPaymentRepository;
    private final S3UploadService s3UploadService;
    private final UserFcmTokenRepository userFcmTokenRepository;
    private final DonationService donationService;

    public Optional<User> findUser(long id) {
        return userRepository.findById(id);
    }

    public List<UserAddress> findUserAddress(Long id) {
        List<UserAddress> userAddressEntity = userAddressRepository.findByUserId(id);
        System.out.println(userAddressEntity);
        return userAddressEntity;
    }

    public UserRes.EditMyPage getEditMyPage(User user) {
        return userConverter.convertToMyPage(user);
    }

    public UserRes.MyPage getMyPage(User user) {
        List<RegularPayment> regularPayments = regularPaymentRepository.findByUser(user);
        Long projectAttentionCnt = projectUserAttentionRepository.countById_userId(user.getId());

        return projectConverter.getMyPage(regularPayments,projectAttentionCnt, user.getNickname());
    }

    public OrderRes.UserDetail getUserInfo(User user) {
        return userConverter.convertToUserInfo(user);
    }

    public UserRes.SignUpInfo getUserSignUpInfo() {
        LocalDate localDate = LocalDate.now();
        Long totalUser = userRepository.countBy();
        Long oneDayUser = userRepository.countByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime.parse(localDate+FIRST_TIME), LocalDateTime.parse(localDate+LAST_TIME));
        Long weekUser = userRepository.countByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime.parse(localDate.minusWeeks(1)+FIRST_TIME) , LocalDateTime.parse(localDate+LAST_TIME));
        Long monthUser = userRepository.countByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime.parse(localDate.with(TemporalAdjusters.firstDayOfMonth())+FIRST_TIME), LocalDateTime.parse(localDate.with(TemporalAdjusters.lastDayOfMonth())+LAST_TIME));

        return userConverter.convertToUserSignUpInfo(oneDayUser,weekUser,monthUser,totalUser);
    }

    @Transactional
    public PageResponse<List<UserRes.UserList>> getUserList(int page, int size, Status status, String content) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserRepository.UserList> userList = null;
        System.out.println(status);
        if(status == null && content ==null) {
            userList = userRepository.getUserList(pageable);
        }
        else if (status !=null && content ==null){
            userList = userRepository.getUserListByStatus(pageable, status.getValue());
        }
        else if(status!=null){
            userList = userRepository.getUserListByStatusAndName(pageable, status.getValue(),content);
        }
        else{
            userList = userRepository.getUserListByName(pageable, content);
        }
        List<UserRes.UserList> userLists = new ArrayList<>();

        userList.getContent().forEach(
                result -> userLists.add(
                        userConverter.convertToUserList(result)
                )
        );

        return new PageResponse<>(userList.isLast(),userList.getTotalElements(),userLists);
    }

    public UserRes.UserAdminDetail getUserAdminDetail(Long userId) {
        UserRepository.UserList userDetail = userRepository.getUserDetail(userId);

        List<OrderRes.UserBillCard> userCards = orderService.getUserBillCard(userId);


        return userConverter.convertToUserAdminDetail(userDetail,userCards);
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

        userRepository.save(user);
    }

    public void saveFcmToken(User user, UserReq.FcmToken token) {
        userFcmTokenRepository.save(userConverter.convertToUserFcm(user, token));
    }

    public void deleteFcmToken(Long userId, String deviceId) {
        userFcmTokenRepository.deleteById(UserFcmPk.builder().userId(userId).deviceId(deviceId).build());
    }

    @Transactional
    public void modifyPhoneNumber(User user, UserReq.ModifyPhone phone) {
        if(!user.getPhoneNumber().equals(phone.getOldPhone())) throw new BadRequestException(NOT_CORRECT_PHONE);
        if(userRepository.existsByPhoneNumber(phone.getNewPhone())) throw new BadRequestException(USERS_EXISTS_PHONE);
        user.setPhoneNumber(phone.getNewPhone());
        userRepository.save(user);

    }

    @Transactional
    public void modifyEmail(User user, UserReq.ModifyEmail email) {
        if(!user.getEmail().equals(email.getOldEmail())) throw new BadRequestException(NOT_CORRECT_EMAIL);
        if(userRepository.existsByEmail(email.getNewEmail())) throw new BadRequestException(ModifyEmailCode.USERS_EXISTS_EMAIL);
        user.setEmail(email.getNewEmail());
        userRepository.save(user);
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

        user = userRepository.save(user);

        return userConverter.convertToAlarmAgree(user);
    }

    @Transactional
    public void postAppleUserInfo(User user, UserReq.AppleUserInfo appleUserInfo) {
        user.updateUserInfo(appleUserInfo.getBirthDate(), appleUserInfo.getName(), appleUserInfo.getPhone());

        userRepository.save(user);
    }

    @RedissonLock(LockName = "유저 탈퇴", key = "#user.id")
    public void deleteUserInfo(User user) {
        user.setStatus(Status.INACTIVE);
        donationService.deleteRegularPayment(user);
        userRepository.save(user);
    }
}

package com.example.matchapi.user.service;

import com.example.matchapi.donation.service.DonationService;
import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.order.service.OrderService;
import com.example.matchapi.project.convertor.ProjectConvertor;
import com.example.matchapi.project.helper.ProjectHelper;
import com.example.matchapi.user.convertor.UserConvertor;
import com.example.matchapi.user.dto.UserReq;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.donation.repository.RegularPaymentRepository;
import com.example.matchdomain.project.entity.ImageRepresentStatus;
import com.example.matchdomain.project.entity.ProjectUserAttention;
import com.example.matchdomain.project.repository.ProjectUserAttentionRepository;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.UserAddress;
import com.example.matchdomain.user.entity.UserFcmToken;
import com.example.matchdomain.user.entity.pk.UserFcmPk;
import com.example.matchdomain.user.exception.ModifyEmailCode;
import com.example.matchdomain.user.repository.UserAddressRepository;
import com.example.matchdomain.user.repository.UserFcmTokenRepository;
import com.example.matchdomain.user.repository.UserRepository;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
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

import static com.example.matchcommon.constants.MatchStatic.*;
import static com.example.matchdomain.donation.entity.DonationStatus.EXECUTION_REFUND;
import static com.example.matchdomain.user.exception.ModifyEmailCode.NOT_CORRECT_EMAIL;
import static com.example.matchdomain.user.exception.ModifyPhoneErrorCode.NOT_CORRECT_PHONE;
import static com.example.matchdomain.user.exception.UserNormalSignUpErrorCode.USERS_EXISTS_EMAIL;
import static com.example.matchdomain.user.exception.UserNormalSignUpErrorCode.USERS_EXISTS_PHONE;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final UserConvertor userConvertor;
    private final DonationUserRepository donationUserRepository;
    private final ProjectConvertor projectConvertor;
    private final ProjectHelper projectHelper;
    private final ProjectUserAttentionRepository projectUserAttentionRepository;
    private final OrderService orderService;
    private final RegularPaymentRepository regularPaymentRepository;
    private final S3UploadService s3UploadService;
    private final UserFcmTokenRepository userFcmTokenRepository;

    public Optional<User> findUser(long id) {
        return userRepository.findById(id);
    }

    public List<UserAddress> findUserAddress(Long id) {
        List<UserAddress> userAddressEntity = userAddressRepository.findByUserId(id);
        System.out.println(userAddressEntity);
        return userAddressEntity;
    }

    public UserRes.EditMyPage getEditMyPage(User user) {
        return userConvertor.toMyPage(user);
    }

    public UserRes.MyPage getMyPage(User user) {
        List<RegularPayment> regularPayments = regularPaymentRepository.findByUser(user);
        Long projectAttentionCnt = projectUserAttentionRepository.countById_userId(user.getId());

        return projectConvertor.getMyPage(regularPayments,projectAttentionCnt, user.getName());
    }

    public OrderRes.UserDetail getUserInfo(User user) {
        return userConvertor.userInfo(user);
    }

    public UserRes.SignUpInfo getUserSignUpInfo() {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        Long totalUser = userRepository.countBy();
        Long oneDayUser = userRepository.countByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime.parse(localDate+FIRST_TIME), LocalDateTime.parse(localDate+LAST_TIME));
        Long weekUser = userRepository.countByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime.parse(localDate.minusWeeks(1)+FIRST_TIME) , LocalDateTime.parse(localDate+LAST_TIME));
        Long monthUser = userRepository.countByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime.parse(localDate.with(TemporalAdjusters.firstDayOfMonth())+FIRST_TIME), LocalDateTime.parse(localDate.with(TemporalAdjusters.lastDayOfMonth())+LAST_TIME));

        return userConvertor.UserSignUpInfo(oneDayUser,weekUser,monthUser,totalUser);
    }

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
                        userConvertor.UserList(result)
                )
        );

        return new PageResponse<>(userList.isLast(),userList.getTotalElements(),userLists);
    }

    public UserRes.UserAdminDetail getUserAdminDetail(Long userId) {
        UserRepository.UserList userDetail = userRepository.getUserDetail(userId);

        List<OrderRes.UserBillCard> userCards = orderService.getUserBillCard(userId);


        return userConvertor.UserAdminDetail(userDetail,userCards);
    }

    public UserRes.Profile getProfile(User user) {
        return userConvertor.UserProfile(user);
    }

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
            user.setName(modifyProfile.getName());
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
        userFcmTokenRepository.save(userConvertor.UserFcm(user, token));
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
}

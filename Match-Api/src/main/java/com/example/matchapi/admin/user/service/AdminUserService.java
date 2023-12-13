package com.example.matchapi.admin.user.service;

import com.example.matchapi.donation.service.DonationService;
import com.example.matchapi.user.converter.UserConverter;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchcommon.annotation.RedissonLock;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.ForbiddenException;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.user.adaptor.UserAdaptor;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.enums.Gender;
import com.example.matchdomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.example.matchdomain.user.exception.UserAuthErrorCode.NOT_EXIST_USER;
import static com.example.matchdomain.user.exception.UserNormalSignUpErrorCode.*;

@Service
@RequiredArgsConstructor
public class AdminUserService {
    private final UserAdaptor userAdaptor;
    private final UserConverter userConverter;
    private final DonationService donationService;

    public UserRes.SignUpInfo getUserSignUpInfo() {
        LocalDate localDate = LocalDate.now();
        Long totalUser = userAdaptor.getTotalUserCnt();
        Long oneDayUser = userAdaptor.getOneDayUserCnt(localDate);
        Long weekUser = userAdaptor.getWeekUserCnt(localDate);
        Long monthUser = userAdaptor.getMonthUserCnt(localDate);
        Long deleteUser = userAdaptor.getDeleteUserCnt();

        return userConverter.convertToUserSignUpInfo(oneDayUser,weekUser,monthUser,totalUser, deleteUser);
    }

    @Transactional
    public PageResponse<List<UserRes.UserList>> getUserList(int page, int size, Status status, String content) {
        Page<UserRepository.UserList> userList = userAdaptor.getUserList(page, size, status, content);

        List<UserRes.UserList> userLists = new ArrayList<>();

        userList.getContent().forEach(
                result -> userLists.add(
                        userConverter.convertToUserList(result)
                )
        );

        return new PageResponse<>(userList.isLast(),userList.getTotalElements(),userLists);
    }

    public UserRes.UserAdminDetail getUserAdminDetail(Long userId) {
        UserRepository.UserList userDetail = userAdaptor.getUserDetail(userId);

        return userConverter.convertToUserAdminDetail(userDetail);
    }

    public User findByUserId(Long userId) {
        return userAdaptor.findByUserId(userId).orElseThrow(() -> new NotFoundException(NOT_EXIST_USER));
    }

    @RedissonLock(LockName = "유저", key = "#user.id")
    public void unActivateUser(User user) {
        updateUserInfo(user.getId(), users -> users.setStatus(Status.INACTIVE));
        donationService.deleteRegularPayment(user);
    }

    public void updateNickname(Long userId, String nickname) {
        updateUserInfo(userId, user -> user.setNickname(nickname));
    }

    public void updatePhone(Long userId, String phone) {
        if (userAdaptor.existsPhoneNumber(phone)) {
            throw new ForbiddenException(USERS_EXISTS_PHONE);
        }
        updateUserInfo(userId, user -> user.setPhoneNumber(phone));
    }

    public void updateEmail(Long userId, String email) {
        if (userAdaptor.existsEmail(email)) {
            throw new ForbiddenException(USERS_EXISTS_EMAIL);
        }
        updateUserInfo(userId, user -> user.setEmail(email));
    }

    public void updateGender(Long userId, Gender gender) {
        updateUserInfo(userId, user -> user.setGender(gender));
    }

    public void updateBirth(Long userId, LocalDate birth) {
        updateUserInfo(userId, user -> user.setBirth(birth));
    }

    private void updateUserInfo(Long userId, Consumer<User> updateFunction) {
        User user = findByUserId(userId);
        updateFunction.accept(user);
        userAdaptor.save(user);
    }
}

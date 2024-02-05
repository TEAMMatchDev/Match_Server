package com.example.matchdomain.user.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.UnauthorizedException;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.enums.SocialType;
import com.example.matchdomain.user.exception.UserAuthErrorCode;
import com.example.matchdomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import static com.example.matchcommon.constants.MatchStatic.FIRST_TIME;
import static com.example.matchcommon.constants.MatchStatic.LAST_TIME;
import static com.example.matchdomain.user.entity.enums.Alarm.ACTIVE;
import static com.example.matchdomain.user.entity.enums.SocialType.APPLE;
import static com.example.matchdomain.user.entity.enums.SocialType.NORMAL;
import static com.example.matchdomain.user.exception.UserLoginErrorCode.NOT_EXIST_USER;

import javax.transaction.Transactional;

@Adaptor
@RequiredArgsConstructor
public class UserAdaptor {
    private final UserRepository userRepository;
    public Optional<User> existsSocialUser(String socialId, SocialType socialType){
        return userRepository.findBySocialIdAndSocialTypeAndStatus(socialId, socialType, Status.ACTIVE);
    }

    public Optional<User> findByUserId(Long userId) {
        return userRepository.findById(userId);
    }

    public List<User> findByServiceAlarm() {
        return userRepository.findByServiceAlarm(ACTIVE);
    }

    public List<User> findDeleteUsers() {
        return userRepository.findByStatus(Status.INACTIVE);
    }

    public User findByUser(String userId) {
        return userRepository.findByIdAndStatus(Long.valueOf(userId), Status.ACTIVE).orElseThrow(()->new BadRequestException(NOT_EXIST_USER));
    }

    public boolean existsEmail(String email) {
        return userRepository.existsByEmailAndStatus(email, Status.ACTIVE);
    }

    public boolean existsPhoneNumber(String phone) {
        return userRepository.existsByPhoneNumberAndStatus(phone, Status.ACTIVE);
    }

    public User findByUserName(String email) {
        return userRepository.findByUsernameAndStatusAndSocialType(email, Status.ACTIVE, NORMAL).orElseThrow(()->new BadRequestException(NOT_EXIST_USER));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public boolean checkEmailPassword(String email, SocialType socialType) {
        return userRepository.existsByUsernameAndSocialTypeAndStatus(email, socialType, Status.ACTIVE);
    }

    public boolean existsEmailAndSocial(String email, SocialType socialType){
        return userRepository.existsByEmailAndSocialTypeNotAndStatus(email, socialType, Status.ACTIVE);
    }

    public User findByUsernameAndStatus(String username){
        return userRepository.findByUsernameAndStatus(username, Status.ACTIVE).orElseThrow(() -> new UnauthorizedException(UserAuthErrorCode.NOT_EXIST_USER));
    }

    public Long getTotalUserCnt(){
        return userRepository.countBy();
    }

    public Long getOneDayUserCnt(LocalDate localDate){
        return userRepository.countByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime.parse(localDate+FIRST_TIME), LocalDateTime.parse(localDate+LAST_TIME));
    }

    public Long getWeekUserCnt(LocalDate localDate) {
        return userRepository.countByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime.parse(localDate.minusWeeks(1)+FIRST_TIME) , LocalDateTime.parse(localDate+LAST_TIME));
    }

    public Long getMonthUserCnt(LocalDate localDate) {
        return userRepository.countByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime.parse(localDate.with(TemporalAdjusters.firstDayOfMonth())+FIRST_TIME), LocalDateTime.parse(localDate.with(TemporalAdjusters.lastDayOfMonth())+LAST_TIME));
    }

    public UserRepository.UserList getUserDetail(Long userId) {
        return userRepository.getUserDetail(userId);
    }

    public Long getDeleteUserCnt() {
        return userRepository.countByStatus(Status.INACTIVE);
    }

    public Page<UserRepository.UserList> getUserAllList(Pageable pageable) {
        return userRepository.getUserList(pageable);
    }

    public Page<UserRepository.UserList> getUserListByEmail(Pageable pageable, String content) {
        return userRepository.getUserListByEmail(pageable, content);
    }

    public Page<UserRepository.UserList> getUserListByPhone(Pageable pageable, String content) {
        return userRepository.getUserListByPhone(pageable, content);
    }

    public Page<UserRepository.UserList> getUserListByName(Pageable pageable, String content) {
        return userRepository.getUserListByName(pageable, content);
    }

    public Page<UserRepository.UserList> getUserListByNickname(Pageable pageable, String content) {
        return userRepository.getUserListByNickname(pageable, content);
    }

    public Page<UserRepository.UserList> getUserListById(Pageable pageable, String content) {
        return userRepository.getUserListById(pageable, content);
    }

}

package com.example.matchapi.user.service;

import com.example.matchapi.order.dto.OrderRes;
import com.example.matchapi.project.convertor.ProjectConvertor;
import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchapi.project.helper.ProjectHelper;
import com.example.matchapi.project.service.ProjectService;
import com.example.matchapi.user.convertor.UserConvertor;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.project.entity.ImageRepresentStatus;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.entity.ProjectUserAttention;
import com.example.matchdomain.project.repository.ProjectRepository;
import com.example.matchdomain.project.repository.ProjectUserAttentionRepository;
import com.example.matchdomain.user.entity.User;
import com.example.matchdomain.user.entity.UserAddress;
import com.example.matchdomain.user.repository.UserAddressRepository;
import com.example.matchdomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.matchcommon.constants.MatchStatic.FIRST_TIME;
import static com.example.matchcommon.constants.MatchStatic.LAST_TIME;
import static com.example.matchdomain.donation.entity.DonationStatus.EXECUTION_REFUND;

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
        List<DonationUser> donationUser = donationUserRepository.findByUserAndDonationStatusNot(user, EXECUTION_REFUND);
        List<ProjectUserAttention> projectList = projectUserAttentionRepository.findById_userIdAndProject_ProjectImage_imageRepresentStatusOrderByCreatedAt(user.getId(),ImageRepresentStatus.REPRESENT);

        return projectConvertor.getMyPage(donationUser,projectList);
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

    public PageResponse<List<UserRes.UserList>> getUserList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<UserRepository.UserList> userList = userRepository.getUserList(pageable);

        List<UserRes.UserList> userLists = new ArrayList<>();

        userList.getContent().forEach(
                result -> userLists.add(
                        userConvertor.UserList(result)
                )
        );

        return new PageResponse<>(userList.isLast(),userList.getTotalElements(),userLists);
    }
}

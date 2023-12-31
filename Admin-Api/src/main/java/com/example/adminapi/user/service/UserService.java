package com.example.adminapi.user.service;

import com.example.adminapi.user.converter.UserConverter;
import com.example.adminapi.user.dto.UserRes;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.temporal.TemporalAdjusters;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private static final String FIRST_TIME = "T00:00:00";
    private static final String LAST_TIME = "T23:59:59";
    public UserRes.SignUpInfo getUserSignUpInfo() {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        Long totalUser = userRepository.countBy();
        Long oneDayUser = userRepository.countByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime.parse(localDate+FIRST_TIME), LocalDateTime.parse(localDate+LAST_TIME));
        Long weekUser = userRepository.countByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime.parse(localDate.minusWeeks(1)+FIRST_TIME) , LocalDateTime.parse(localDate+LAST_TIME));
        Long monthUser = userRepository.countByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime.parse(localDate.with(TemporalAdjusters.firstDayOfMonth())+FIRST_TIME), LocalDateTime.parse(localDate.with(TemporalAdjusters.lastDayOfMonth())+LAST_TIME));

        return userConverter.UserSignUpInfo(oneDayUser,weekUser,monthUser,totalUser);
    }

    public PageResponse<List<UserRes.UserList>> getUserList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<UserRepository.UserList> userList = userRepository.getUserList(pageable);

        List<UserRes.UserList> userLists = new ArrayList<>();

        userList.getContent().forEach(
                result -> userLists.add(
                        userConverter.UserList(result)
                )
        );

        return new PageResponse<>(userList.isLast(),userList.getTotalElements(),userLists);
    }
}

package com.example.adminapi.user.service;

import com.example.adminapi.user.convertor.UserConvertor;
import com.example.adminapi.user.dto.UserRes;
import com.example.matchdomain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.temporal.TemporalAdjusters;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConvertor userConvertor;
    private static final String FIRST_TIME = "T00:00:00";
    private static final String LAST_TIME = "T23:59:59";
    public UserRes.SignUpInfo getUserSignUpInfo() {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.now();
        Long totalUser = userRepository.countBy();
        Long oneDayUser = userRepository.countByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime.parse(localDate+FIRST_TIME), LocalDateTime.parse(localDate+LAST_TIME));
        Long weekUser = userRepository.countByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime.parse(localDate.minusWeeks(1)+FIRST_TIME) , LocalDateTime.parse(localDate+LAST_TIME));
        Long monthUser = userRepository.countByCreatedAtGreaterThanAndCreatedAtLessThan(LocalDateTime.parse(localDate.with(TemporalAdjusters.firstDayOfMonth())+FIRST_TIME), LocalDateTime.parse(localDate.with(TemporalAdjusters.lastDayOfMonth())+LAST_TIME));

        return userConvertor.UserSignUpInfo(oneDayUser,weekUser,monthUser,totalUser);
    }

}

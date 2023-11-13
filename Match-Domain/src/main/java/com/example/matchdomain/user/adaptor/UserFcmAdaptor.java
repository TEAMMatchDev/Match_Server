package com.example.matchdomain.user.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.constants.enums.Topic;
import com.example.matchdomain.user.entity.UserFcmToken;
import com.example.matchdomain.user.repository.UserFcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.matchdomain.user.entity.enums.Alarm.ACTIVE;

@Adaptor
@RequiredArgsConstructor
public class UserFcmAdaptor {
    private final UserFcmTokenRepository fcmTokenRepository;

    public List<UserFcmToken> findByUser(Long userId) {
        return fcmTokenRepository.findByUserIdAndUser_ServiceAlarm(userId, ACTIVE);
    }

    public Page<UserFcmToken> findByUserServiceAlarm(int page, int size, Topic topic) {
        Pageable pageable = PageRequest.of(page, size);
        if(topic.equals(Topic.PROJECT_UPLOAD)) {
            return fcmTokenRepository.findByUser_ServiceAlarm(ACTIVE, pageable);
        }else{
            return fcmTokenRepository.findByUser_EventAlarm(ACTIVE, pageable);
        }
    }
}

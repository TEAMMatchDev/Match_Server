package com.example.matchapi.common.util;

import com.example.matchcommon.annotation.Helper;
import com.example.matchdomain.user.adaptor.UserAdaptor;
import com.example.matchdomain.user.adaptor.UserFcmAdaptor;
import com.example.matchdomain.user.entity.UserFcmToken;
import com.example.matchinfrastructure.fcm.converter.FcmConverter;
import com.example.matchinfrastructure.fcm.dto.FCMNotificationRequestDto;
import com.example.matchcommon.constants.enums.Topic;
import com.example.matchinfrastructure.fcm.service.FcmNotificationService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.TopicManagementResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.stream.Collectors;

@Helper
@Slf4j
@RequiredArgsConstructor
public class MessageHelper {
    private final FcmNotificationService firebaseService;
    private final FcmConverter fcmConverter;
    private final UserAdaptor userAdaptor;
    private final UserFcmAdaptor userFcmAdaptor;

    @Async("topic-fcm")
    public void helpFcmMessage(String body, Topic topic, Long id) {
        FCMNotificationRequestDto fcmNotificationRequestDto = fcmConverter.convertToFcmTopicMessageDto(body);

        int page = 0, size = 1000;

        Page<UserFcmToken> userFcmTokens = userFcmAdaptor.findByUserServiceAlarm(page, size, topic);

        subscribeTopic(convertToList(userFcmTokens.getContent()), topic);

        if(!userFcmTokens.isLast()){
            for(int i = 1; i < userFcmTokens.getTotalPages(); i++){
                userFcmTokens = userFcmAdaptor.findByUserServiceAlarm(i, size, topic);
                subscribeTopic(convertToList(userFcmTokens.getContent()),topic);
            }
        }

        firebaseService.sendTopicNotification(fcmNotificationRequestDto, topic, id);
    }

    private void subscribeTopic(List<String> tokensList, Topic topic){
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(
                    tokensList, topic.getTopic()
            );
            log.info("구독 성공 토큰 갯수 : " + response.getSuccessCount());
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<String> convertToList(List<UserFcmToken> userFcmTokens){
        return userFcmTokens.stream().map(UserFcmToken::getFcmToken).collect(Collectors.toList());
    }
}

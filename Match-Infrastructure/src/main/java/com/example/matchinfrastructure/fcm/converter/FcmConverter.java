package com.example.matchinfrastructure.fcm.converter;

import com.example.matchcommon.annotation.Converter;
import com.example.matchinfrastructure.fcm.dto.AlertType;
import com.example.matchinfrastructure.fcm.dto.FCMNotificationRequestDto;
import com.example.matchinfrastructure.fcm.dto.NotificationPayDto;
import com.example.matchcommon.constants.enums.Topic;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import java.util.Map;

import static com.example.matchcommon.constants.MatchAlertStatic.PAY_ALERT_BODY;
import static com.example.matchcommon.constants.MatchAlertStatic.ALERT_TITLE;

@Converter
public class FcmConverter {

    public FCMNotificationRequestDto convertToDto(String inherenceName) {
        return FCMNotificationRequestDto
                .builder()
                .title(ALERT_TITLE)
                .body(inherenceName + PAY_ALERT_BODY)
                .build();
    }

    public NotificationPayDto convertToPayData() {
        return NotificationPayDto.builder().screen(AlertType.HOME_SCREEN).build();
    }

    public Notification convertToNotification(FCMNotificationRequestDto fcmNotificationRequestDto) {
        return Notification
                .builder()
                .setTitle(fcmNotificationRequestDto.getTitle())
                .setBody(fcmNotificationRequestDto.getBody())
                .build();
    }

    public Message convertToTopicMessage(Notification notification, Topic topic, Map<String, String> data){
        return Message
                .builder()
                .setTopic(topic.getTopic())
                .setNotification(notification)
                .putAllData(data)
                .build();
    }

    public Message convertToDataMessage(Notification notification, Map<String, String> data, FCMNotificationRequestDto fcmNotificationRequestDto){
        return Message
                .builder()
                .setToken(fcmNotificationRequestDto.getToken())
                .setNotification(notification)
                .putAllData(data)
                .build();
    }

    public FCMNotificationRequestDto convertToFcmTopicMessageDto(String body) {
        return FCMNotificationRequestDto
                .builder()
                .title(ALERT_TITLE)
                .body(body)
                .build();
    }
}

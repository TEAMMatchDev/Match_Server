package com.example.matchinfrastructure.fcm.service;

import com.example.matchinfrastructure.fcm.converter.FcmConverter;
import com.example.matchinfrastructure.fcm.dto.FCMNotificationRequestDto;
import com.example.matchinfrastructure.fcm.dto.NotificationPayDto;
import com.example.matchcommon.constants.enums.Topic;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.example.matchinfrastructure.fcm.dto.AlertType.EVENT_SCREEN;
import static com.example.matchinfrastructure.fcm.dto.AlertType.PROJECT_SCREEN;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmNotificationService {
    private final FcmConverter fcmConverter;
    @PostConstruct
    public void init() {
        log.info("init");
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(
                            GoogleCredentials
                                    .fromStream(new ClassPathResource("serviceAccountKey.json").getInputStream())
                                    .createScoped(List.of("https://www.googleeapis.com/auth/cloud-platform")))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        } catch (IOException e) {
            log.info(e.getMessage());
            log.info("FireBase Config Failed");
            // spring 뜰때 알림 서버가 잘 동작하지 않는 것이므로 바로 죽임
            throw new RuntimeException(e.getMessage());

        }
    }

    public void testNotification(FCMNotificationRequestDto fcmNotificationRequestDto){
        Notification notification = fcmConverter.convertToNotification(fcmNotificationRequestDto);

        Message message = Message
                .builder()
                .setToken(fcmNotificationRequestDto.getToken())
                .setNotification(notification)
                .build();

        sendNotification(message);

    }


    public void sendNotificationRegularPayments(FCMNotificationRequestDto fcmNotificationRequestDto, NotificationPayDto notificationPayDto){
        Notification notification = fcmConverter.convertToNotification(fcmNotificationRequestDto);


        Map<String, String> data = new HashMap<>();
        data.put("screen", notificationPayDto.getScreen().toString());

        Message message = fcmConverter.convertToDataMessage(notification, data, fcmNotificationRequestDto);

        sendNotification(message);


    }

    public void sendTopicNotification(FCMNotificationRequestDto fcmNotificationRequestDto, Topic topic, Long id){
        Notification notification = fcmConverter.convertToNotification(fcmNotificationRequestDto);

        Map<String, String> data = new HashMap<>();
        if(topic.equals(Topic.PROJECT_UPLOAD)){
            data.put("screen", String.valueOf(PROJECT_SCREEN));
            data.put("projectId", String.valueOf(id));
        }
        else{
            data.put("screen", String.valueOf(EVENT_SCREEN));
            data.put("eventId", String.valueOf(id));
        }

        Message message = fcmConverter.convertToTopicMessage(notification, topic, data);

        sendNotification(message);
    }

    @Async("fcm")
    public void sendNotification(Message message){
        try {
            String result = FirebaseMessaging.getInstance().send(message);
            log.info(result);
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}

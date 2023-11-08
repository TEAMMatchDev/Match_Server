package com.example.matchinfrastructure.fcm.service;

import com.example.matchinfrastructure.fcm.dto.AlertType;
import com.example.matchinfrastructure.fcm.dto.FCMNotificationRequestDto;
import com.example.matchinfrastructure.fcm.dto.NotificationPayDto;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmNotificationService {
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

    @Async("fcm")
    public void testNotification(FCMNotificationRequestDto fcmNotificationRequestDto){
        Notification notification = Notification
                .builder()
                .setTitle(fcmNotificationRequestDto.getTitle())
                .setBody(fcmNotificationRequestDto.getBody())
                .build();

        Message message = Message
                .builder()
                .setToken(fcmNotificationRequestDto.getToken())
                .setNotification(notification)
                .putData("click_action","")
                .build();

        try {
            String result = FirebaseMessaging.getInstance().send(message);
            System.out.println(result);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }

    }


    @Async("fcm")
    public void sendNotificationRegularPayments(FCMNotificationRequestDto fcmNotificationRequestDto, NotificationPayDto notificationPayDto){
        Notification notification = Notification
                .builder()
                .setTitle(fcmNotificationRequestDto.getTitle())
                .setBody(fcmNotificationRequestDto.getBody())
                .build();

        Map<String, String> data = new HashMap<>();
        data.put("screen", notificationPayDto.getScreen().toString());

        Message message = Message
                .builder()
                .setToken(fcmNotificationRequestDto.getToken())
                .setNotification(notification)
                .putAllData(data)
                .build();

        try {
            String result = FirebaseMessaging.getInstance().send(message);
            System.out.println(result);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public NotificationPayDto convertToPayData() {
        return NotificationPayDto.builder().screen(AlertType.HOME_SCREEN).build();
    }
}

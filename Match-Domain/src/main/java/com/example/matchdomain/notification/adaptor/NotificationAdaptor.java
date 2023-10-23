package com.example.matchdomain.notification.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchdomain.notification.entity.Notification;
import com.example.matchdomain.notification.repository.NotificationRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Adaptor
@RequiredArgsConstructor
public class NotificationAdaptor {
    private final NotificationRepository notificationRepository;

    public Page<Notification> findByUser(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return notificationRepository.findByUserOrderByCreatedAt(user, pageable);
    }

    public int countByUnRead(User user) {
        return notificationRepository.countByUserAndIsRead(user, false);
    }

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }
}

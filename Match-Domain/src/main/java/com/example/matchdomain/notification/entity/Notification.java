package com.example.matchdomain.notification.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.event.entity.Event;
import com.example.matchdomain.notification.enums.NotificationType;
import com.example.matchdomain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "Notification")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@BatchSize(size = 100)
public class Notification extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false, insertable=false, updatable=false)
    private User user;

    @Column(name="userId")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String title;

    private String body;

    private boolean isRead = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId",nullable = false, insertable=false, updatable=false)
    private Event event;

    @Column(name="eventId")
    private Long eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donationUserId",nullable = false, insertable=false, updatable=false)
    private DonationUser donationUser;

    @Column(name="donationUserId")
    private Long donationUserId;

    public boolean getIsRead() {
        return isRead;
    }
}

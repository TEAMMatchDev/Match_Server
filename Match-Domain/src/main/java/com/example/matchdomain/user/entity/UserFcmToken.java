package com.example.matchdomain.user.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.user.entity.pk.UserFcmPk;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "UserFcmToken")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class UserFcmToken extends BaseEntity {

    @EmbeddedId
    private UserFcmPk userFcmPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false,insertable=false, updatable=false)
    private User user;

    @Column(name = "fcmToken")
    private String fcmToken;

}

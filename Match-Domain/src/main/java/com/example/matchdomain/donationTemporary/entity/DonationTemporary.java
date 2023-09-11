package com.example.matchdomain.donationTemporary.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "DonationTemporary")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class DonationTemporary extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false, insertable=false, updatable=false)
    private User user;

    @Column(name="userId")
    private Long userId;

    private String name;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private AlarmMethod alarmMethod;

    @Enumerated(EnumType.STRING)
    private DonationKind donationKind;

    @Enumerated(EnumType.STRING)
    private Deposit deposit = Deposit.NONEXISTENCE;
}

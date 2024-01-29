package com.example.matchdomain.donation.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.enums.RegularPayStatus;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.matchdomain.donation.entity.enums.RegularPayStatus.PROCEEDING;

@Entity
@Table(name = "RegularPayment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@BatchSize(size = 100)
public class RegularPayment extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false, insertable=false, updatable=false)
    private User user;

    @Column(name="userId")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId",nullable = false, insertable=false, updatable=false)
    private Project project;

    private Long projectId;

    private int payDate;

    //정기 결제 금액
    private Long amount;

    @Enumerated(EnumType.STRING)
    private RegularPayStatus regularPayStatus = PROCEEDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userCardId",nullable = false, insertable=false, updatable=false)
    private UserCard userCard;

    @Column(name="userCardId")
    private Long userCardId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "regularPaymentId")
    @BatchSize(size = 100)
    private List<RequestFailedHistory> requestFailedHistories = new ArrayList<>();


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "regularPaymentId")
    @BatchSize(size = 100)
    private List<DonationUser> donationUser = new ArrayList<>();
}

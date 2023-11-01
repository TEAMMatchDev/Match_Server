package com.example.matchdomain.donation.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.donation.entity.enums.PaymentStatus;
import com.example.matchdomain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "RequestPaymentHistory")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
//정기 결제 시도 내역 과 성공, 실패, 취소 저장 -> 실패 시 계속 시도해야 하므로
public class RequestPaymentHistory extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false, insertable=false, updatable=false)
    private User user;

    @Column(name="userId")
    private Long userId;

    private String tid;

    private String orderId;

    private Long amount;

    private String reason;

    private int payDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regularPaymentId",nullable = false, insertable=false, updatable=false)
    private RegularPayment regularPayment;

    private Long regularPaymentId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userCardId",nullable = false, insertable=false, updatable=false)
    private UserCard userCard;

    @Column(name="userCardId")
    private Long userCardId;
}

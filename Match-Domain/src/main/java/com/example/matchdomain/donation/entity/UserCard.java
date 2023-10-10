package com.example.matchdomain.donation.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.donation.entity.enums.CardAbleStatus;
import com.example.matchdomain.donation.entity.enums.CardCode;
import com.example.matchdomain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "UserCard")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class UserCard extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false, insertable=false, updatable=false)
    private User user;

    @Column(name="userId")
    private Long userId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userCardId")
    private List<RegularPayment> regularPayment = new ArrayList<>();

    //주문번호
    private String orderId;
    //빌키 번호
    private String bid;

    private String customerId;

    private String cardNo;

    private String expYear;

    private String expMonth;

    private String idNo;

    private String cardPw;

    private CardCode cardCode;

    private String cardName;

    @Enumerated(EnumType.STRING)
    private CardAbleStatus cardAbleStatus = CardAbleStatus.ABLE;
}

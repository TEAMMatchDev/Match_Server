package com.example.matchdomain.user.entity;

import com.example.matchdomain.common.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "UserAddress")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class UserAddress extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false, insertable=false, updatable=false)
    private User user;

    @Column(name="userId")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "isDefault")
    private Boolean isDefault;

    @Enumerated(EnumType.STRING)
    private AddresslType addresslType;

    @Column(name = "baseAddress")
    private String baseAddress;

    @Column(name = "detailAddress")
    private String detailAddress;

    @Column(name = "receiverName")
    private String receiverName;

    @Column(name = "addressPhoneNumber")
    private String addressPhoneNumber;

    @Column(name = "zoneNumber")
    private String zoneNumber;

    @Column(name = "zipCode")
    private String zipCode;


}

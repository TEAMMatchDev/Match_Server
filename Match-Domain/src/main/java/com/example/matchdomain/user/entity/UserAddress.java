package com.example.matchdomain.user.entity;

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
public class UserAddress {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false, insertable=false, updatable=false)
    private User user;

    @Column(name="userId")
    private Long userId;




}

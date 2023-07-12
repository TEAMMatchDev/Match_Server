package com.example.matchdomain.donation.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "DonationRequest")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class DonationRequest extends BaseEntity {
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

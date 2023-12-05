package com.example.matchdomain.review.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.donation.entity.DonationUser;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "Review")
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@BatchSize(size = 10)
public class Review extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donationId",nullable = false, insertable=false, updatable=false)
    private DonationUser donationUser;

    private Long donationId;

    private int donation;

    private int transparency;

    private int information;

    private String comment;
}

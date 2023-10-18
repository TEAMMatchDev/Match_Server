package com.example.matchdomain.event.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.donation.entity.DonationUser;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "User")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@BatchSize(size = 100)
@DynamicInsert
public class Event extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventImg;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donationId",nullable = false, insertable=false, updatable=false)
    private DonationUser donationUser;

    @Column(name="donationId")
    private Long donationId;

}

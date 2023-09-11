package com.example.matchdomain.donationTemporary.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "DonationList")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@BatchSize(size = 100)
public class DonationList extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donationTemporaryId", nullable = false, insertable = false, updatable = false)
    private DonationTemporary donationTemporary;

    @Column(name = "donationTemporaryId")
    private Long donationTemporaryId;

    private String name;

    private int amount;

}
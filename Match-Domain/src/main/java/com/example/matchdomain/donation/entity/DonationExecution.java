package com.example.matchdomain.donation.entity;
/*
집행 관련 돈 사용
 */

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.donation.entity.enums.Execution;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "DonationExecution")
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class DonationExecution extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Comment("집행 전체인지 일부 인지 구분")
    private Execution execution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donationUserId",nullable = false, insertable=false, updatable=false)
    private DonationUser donationUser;

    @Column(name="donationUserId")
    @Comment("집행 할 대상")
    private Long donationUserId;

    @Comment("집행 금액")
    private Long price;
}

package com.example.matchdomain.donation.entity;

import com.example.matchdomain.common.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DonationHistory")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@BatchSize(size = 100)
public class DonationHistory extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private HistoryStatus historyStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donationUserId",nullable = false, insertable=false, updatable=false)
    private DonationUser donationUser;

    @Column(name="donationUserId")
    private Long donationUserId;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "donationHistoryId")
    @BatchSize(size = 100)
    private List<HistoryImage> historyImages = new ArrayList<>();
}

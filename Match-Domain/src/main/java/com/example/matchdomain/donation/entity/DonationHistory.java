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
@BatchSize(size = 1000)
public class DonationHistory extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cnt;

    @Enumerated(EnumType.STRING)
    private HistoryStatus historyStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donationUserId",nullable = false, insertable=false, updatable=false)
    private DonationUser donationUser;

    @Column(name="donationUserId")
    private Long donationUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regularPaymentId",nullable = false, insertable=false, updatable=false)
    private RegularPayment regularPayment;

    @Column(name="regularPaymentId")
    private Long regularPaymentId;

    private String flameImage;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "donationHistoryId")
    private List<HistoryImage> historyImages = new ArrayList<>();

}

package com.example.matchdomain.donation.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.donation.entity.enums.DonationStatus;
import com.example.matchdomain.donation.entity.enums.PayMethod;
import com.example.matchdomain.donation.entity.enums.RegularStatus;
import com.example.matchdomain.donation.entity.flameEnum.FlameType;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DonationUser",indexes = @Index(columnList = "inherenceName"))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@BatchSize(size = 1000)
public class DonationUser extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",nullable = false, insertable=false, updatable=false)
    private User user;

    @Column(name="userId")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId",nullable = false, insertable=false, updatable=false)
    private Project project;

    @Column(name="projectId")
    private Long projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regularPaymentId",nullable = false, insertable=false, updatable=false)
    private RegularPayment regularPayment;

    private Long regularPaymentId;

    @Column(name="price")
    private Long price;

    private String tid;

    private String orderId;

    private String inherenceNumber;

    private String inherenceName;

    @Enumerated(EnumType.STRING)
    private DonationStatus donationStatus;

    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;

    @Enumerated(EnumType.STRING)
    private RegularStatus regularStatus;

    private String flameImage;

    @Enumerated(EnumType.STRING)
    private FlameType flameType = FlameType.NORMAL_FLAME;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "donationUserId")
    @BatchSize(size = 100)
    private List<DonationHistory> donationHistories = new ArrayList<>();

    public void updateInherenceNumber(String inherenceNumber, String flameName) {
        this.inherenceNumber = inherenceNumber;
        this.inherenceName = flameName;
    }

    public void updateDonationStatus(DonationStatus donationStatus) {
        this.donationStatus = donationStatus;
    }
}

package com.example.matchdomain.donation.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "DonationUser",indexes = @Index(columnList = "inherenceName"))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
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

    @Column(name="price")
    private int price;

    private String tid;

    private String orderId;

    private String inherenceNumber;

    private String inherenceName;

    @Enumerated(EnumType.STRING)
    private DonationStatus donationStatus;

    @Enumerated(EnumType.STRING)
    private PayMethod payMethod;

    public void updateInherenceNumber(String inherenceNumber, String flameName) {
        this.inherenceNumber = inherenceNumber;
        this.inherenceName = flameName;
    }
}

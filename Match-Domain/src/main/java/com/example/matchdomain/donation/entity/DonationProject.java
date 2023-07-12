package com.example.matchdomain.donation.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.user.entity.UserAddress;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DonationProject")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class DonationProject extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String projectName;

    @Column(name="projectExplanation")
    private String projectExplanation;

    @Column(name = "contents",columnDefinition = "기부금 사용처")
    private String contents;

    @Column(name = "startedAt")
    private LocalDateTime startedAt;

    @Column(name = "finishedAt")
    private LocalDateTime finishedAt;

    @Enumerated(EnumType.STRING)
    private DonationStatus donationStatus;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "projectId")
    private List<DonationUser> donationUser = new ArrayList<>();

}

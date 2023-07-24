package com.example.matchdomain.project.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.user.entity.SocialType;
import com.example.matchdomain.user.entity.UserStatus;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "Project")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class Project extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "projectName", nullable = false)
    private String projectName;

    @Column(name="projectExplanation")
    private String projectExplanation;

    @Column(name = "viewCnt")
    private int viewCnt;
    //사용처
    @Column(name = "usages")
    private String usages;

    @Column(name = "startedAt")
    private LocalDateTime startedAt;

    @Column(name = "finishedAt")
    private LocalDateTime finishedAt;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    //정기 휴원 유무
    @Enumerated(EnumType.STRING)
    private RegularStatus regularStatus;


    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "projectId")
    private List<DonationUser> donationUser = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "projectId")
    @Fetch(FetchMode.JOIN) // Use @Fetch to fetch the projectImage entities eagerly
    private List<ProjectImage> projectImage = new ArrayList<>();

    public Project(Long id, String projectName, String usages, List<ProjectImage> projectImage) {
        this.id = id;
        this.usages = usages;
        this.projectName = projectName;
        this.projectImage = projectImage;
    }
}

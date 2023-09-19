package com.example.matchdomain.project.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.RegularStatus;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
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
@BatchSize(size = 100)
public class Project extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "projectName")
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

    private String searchKeyword;

    @Enumerated(EnumType.STRING)
    private ProjectKind projectKind;

    @Enumerated(EnumType.STRING)
    private RegularStatus regularStatus;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "projectId")
    @BatchSize(size = 100)
    private List<DonationUser> donationUser = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "projectId")
    @Fetch(FetchMode.JOIN)
    private List<ProjectImage> projectImage = new ArrayList<>();


    public Project(Long id, String projectName, String usages, List<ProjectImage> projectImage) {
        this.id = id;
        this.usages = usages;
        this.projectName = projectName;
        this.projectImage = projectImage;
    }

    public void modifyProject(String projectName, String usages, String detail, RegularStatus regularStatus, LocalDateTime startDate, LocalDateTime endDate, ProjectKind projectKind, String searchKeyword) {
        this.projectName = projectName;
        this.usages = usages;
        this.projectExplanation = detail;
        this.regularStatus = regularStatus;
        this.startedAt = startDate;
        this.finishedAt = endDate;
        this.projectKind = projectKind;
        this.searchKeyword = searchKeyword;
    }
}

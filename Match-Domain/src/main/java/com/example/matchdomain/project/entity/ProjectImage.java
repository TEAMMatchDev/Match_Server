package com.example.matchdomain.project.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.project.entity.enums.ImageRepresentStatus;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "ProjectImage")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class ProjectImage extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sequence")
    private int sequence;

    @Enumerated(EnumType.STRING)
    private ImageRepresentStatus imageRepresentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId",nullable = false, insertable=false, updatable=false)
    private Project project;

    @Column(name="projectId")
    private Long projectId;

    @Column(name= "url")
    private String url;
}

package com.example.matchdomain.project.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.project.entity.pk.ProjectUserAttentionPk;
import com.example.matchdomain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "ProjectUserAttention")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class ProjectUserAttention extends BaseEntity {
    @EmbeddedId
    private ProjectUserAttentionPk id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false, insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId", nullable = false, insertable = false, updatable = false)
    private Project project;


}

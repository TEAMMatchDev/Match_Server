package com.example.matchdomain.project.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.project.entity.enums.ReportReason;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "CommentReport")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@BatchSize(size = 100)
public class CommentReport extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentId",nullable = false, insertable=false, updatable=false)
    private ProjectComment projectComment;

    @Column(name="commentId")
    private Long commentId;

    @Enumerated(EnumType.STRING)
    private ReportReason reportReason;
}

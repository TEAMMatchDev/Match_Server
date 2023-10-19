package com.example.matchdomain.notice.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.common.model.ContentsType;
import com.example.matchdomain.event.entity.Event;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "NoticeContent")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@BatchSize(size = 100)
@DynamicInsert
public class NoticeContent extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noticeId",nullable = false, insertable=false, updatable=false)
    private Notice notice;

    @Column(name="noticeId")
    private Long noticeId;
}

package com.example.matchdomain.notice.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.notice.enums.NoticeType;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Notice")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@BatchSize(size = 10)
@DynamicInsert
public class Notice extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "noticeId")
    @BatchSize(size = 20)
    private List<NoticeContent> noticeContents = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;

    private String title;

	public void updateNotice(String title, NoticeType noticeType) {
        this.title = title;
        this.noticeType = noticeType;
	}
}

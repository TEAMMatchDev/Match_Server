package com.example.matchdomain.banner.entity;

import java.time.LocalDateTime;

import com.example.matchdomain.banner.enums.BannerType;
import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.event.entity.Event;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "`Banner`")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@BatchSize(size = 100)
@DynamicInsert
@ToString
public class Banner extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BannerType bannerType;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId",nullable = false, insertable=false, updatable=false)
    private Event event;

    @Column(name="eventId")
    private Long eventId;

    private String bannerImg;

    private String contentsUrl;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    public void updateBanner(String name, LocalDateTime startDate, LocalDateTime endDate, String bannerImg,
        String contentsUrl) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bannerImg = bannerImg;
        this.contentsUrl = contentsUrl;
    }
}

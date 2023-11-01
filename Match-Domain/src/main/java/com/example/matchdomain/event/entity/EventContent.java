package com.example.matchdomain.event.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.common.model.ContentsEntity;
import com.example.matchdomain.common.model.ContentsType;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "EventContent")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@BatchSize(size = 100)
@DynamicInsert
public class EventContent extends ContentsEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", nullable = false, insertable = false, updatable = false)
    private Event event;

    @Column(name = "eventId")
    private Long eventId;
}

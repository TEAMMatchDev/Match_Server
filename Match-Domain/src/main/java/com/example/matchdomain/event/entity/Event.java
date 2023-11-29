package com.example.matchdomain.event.entity;

import com.example.matchdomain.common.model.BaseEntity;
import com.example.matchdomain.event.enums.EventType;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Event")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@BatchSize(size = 20)
@DynamicInsert
public class Event extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "eventId")
    @BatchSize(size = 20)
    private List<EventContent> eventContents = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private String title;

    private String smallTitle;

    private String thumbnail;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventEndDate;
}

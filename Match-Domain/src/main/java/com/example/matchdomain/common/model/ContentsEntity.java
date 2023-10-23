package com.example.matchdomain.common.model;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class ContentsEntity extends BaseEntity{
    @Enumerated(EnumType.STRING)
    private ContentsType contentsType;

    private String contents;
}

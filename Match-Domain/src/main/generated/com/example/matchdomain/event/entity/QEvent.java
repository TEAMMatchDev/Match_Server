package com.example.matchdomain.event.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEvent is a Querydsl query type for Event
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEvent extends EntityPathBase<Event> {

    private static final long serialVersionUID = 1559356633L;

    public static final QEvent event = new QEvent("event");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<EventContent, QEventContent> eventContents = this.<EventContent, QEventContent>createList("eventContents", EventContent.class, QEventContent.class, PathInits.DIRECT2);

    public final DatePath<java.time.LocalDate> eventEndDate = createDate("eventEndDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> eventStartDate = createDate("eventStartDate", java.time.LocalDate.class);

    public final EnumPath<com.example.matchdomain.event.enums.EventType> eventType = createEnum("eventType", com.example.matchdomain.event.enums.EventType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath smallTitle = createString("smallTitle");

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    public final StringPath thumbnail = createString("thumbnail");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QEvent(String variable) {
        super(Event.class, forVariable(variable));
    }

    public QEvent(Path<? extends Event> path) {
        super(path.getType(), path.getMetadata());
    }

    public QEvent(PathMetadata metadata) {
        super(Event.class, metadata);
    }

}


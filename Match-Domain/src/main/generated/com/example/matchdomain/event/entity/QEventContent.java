package com.example.matchdomain.event.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEventContent is a Querydsl query type for EventContent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEventContent extends EntityPathBase<EventContent> {

    private static final long serialVersionUID = -1749782176L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEventContent eventContent = new QEventContent("eventContent");

    public final com.example.matchdomain.common.model.QContentsEntity _super = new com.example.matchdomain.common.model.QContentsEntity(this);

    //inherited
    public final StringPath contents = _super.contents;

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.ContentsType> contentsType = _super.contentsType;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QEvent event;

    public final NumberPath<Long> eventId = createNumber("eventId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QEventContent(String variable) {
        this(EventContent.class, forVariable(variable), INITS);
    }

    public QEventContent(Path<? extends EventContent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEventContent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEventContent(PathMetadata metadata, PathInits inits) {
        this(EventContent.class, metadata, inits);
    }

    public QEventContent(Class<? extends EventContent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.event = inits.isInitialized("event") ? new QEvent(forProperty("event")) : null;
    }

}


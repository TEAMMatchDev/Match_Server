package com.example.matchdomain.banner.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBanner is a Querydsl query type for Banner
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBanner extends EntityPathBase<Banner> {

    private static final long serialVersionUID = 558390347L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBanner banner = new QBanner("banner");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    public final StringPath bannerImg = createString("bannerImg");

    public final EnumPath<com.example.matchdomain.banner.enums.BannerType> bannerType = createEnum("bannerType", com.example.matchdomain.banner.enums.BannerType.class);

    public final StringPath contentsUrl = createString("contentsUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final com.example.matchdomain.event.entity.QEvent event;

    public final NumberPath<Long> eventId = createNumber("eventId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final DateTimePath<java.time.LocalDateTime> startDate = createDateTime("startDate", java.time.LocalDateTime.class);

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBanner(String variable) {
        this(Banner.class, forVariable(variable), INITS);
    }

    public QBanner(Path<? extends Banner> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBanner(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBanner(PathMetadata metadata, PathInits inits) {
        this(Banner.class, metadata, inits);
    }

    public QBanner(Class<? extends Banner> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.event = inits.isInitialized("event") ? new com.example.matchdomain.event.entity.QEvent(forProperty("event")) : null;
    }

}


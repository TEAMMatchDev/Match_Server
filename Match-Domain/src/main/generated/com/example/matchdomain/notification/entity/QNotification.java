package com.example.matchdomain.notification.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNotification is a Querydsl query type for Notification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotification extends EntityPathBase<Notification> {

    private static final long serialVersionUID = 1041968777L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNotification notification = new QNotification("notification");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    public final StringPath body = createString("body");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.example.matchdomain.donation.entity.QDonationUser donationUser;

    public final NumberPath<Long> donationUserId = createNumber("donationUserId", Long.class);

    public final com.example.matchdomain.event.entity.QEvent event;

    public final NumberPath<Long> eventId = createNumber("eventId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isRead = createBoolean("isRead");

    public final EnumPath<com.example.matchdomain.notification.enums.NotificationType> notificationType = createEnum("notificationType", com.example.matchdomain.notification.enums.NotificationType.class);

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.matchdomain.user.entity.QUser user;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QNotification(String variable) {
        this(Notification.class, forVariable(variable), INITS);
    }

    public QNotification(Path<? extends Notification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNotification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNotification(PathMetadata metadata, PathInits inits) {
        this(Notification.class, metadata, inits);
    }

    public QNotification(Class<? extends Notification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.donationUser = inits.isInitialized("donationUser") ? new com.example.matchdomain.donation.entity.QDonationUser(forProperty("donationUser"), inits.get("donationUser")) : null;
        this.event = inits.isInitialized("event") ? new com.example.matchdomain.event.entity.QEvent(forProperty("event")) : null;
        this.user = inits.isInitialized("user") ? new com.example.matchdomain.user.entity.QUser(forProperty("user")) : null;
    }

}


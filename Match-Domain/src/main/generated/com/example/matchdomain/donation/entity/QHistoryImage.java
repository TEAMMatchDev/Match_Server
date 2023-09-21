package com.example.matchdomain.donation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHistoryImage is a Querydsl query type for HistoryImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHistoryImage extends EntityPathBase<HistoryImage> {

    private static final long serialVersionUID = -1326762260L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHistoryImage historyImage = new QHistoryImage("historyImage");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QDonationHistory donationHistory;

    public final NumberPath<Long> donationHistoryId = createNumber("donationHistoryId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imgUrl = createString("imgUrl");

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QHistoryImage(String variable) {
        this(HistoryImage.class, forVariable(variable), INITS);
    }

    public QHistoryImage(Path<? extends HistoryImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHistoryImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHistoryImage(PathMetadata metadata, PathInits inits) {
        this(HistoryImage.class, metadata, inits);
    }

    public QHistoryImage(Class<? extends HistoryImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.donationHistory = inits.isInitialized("donationHistory") ? new QDonationHistory(forProperty("donationHistory"), inits.get("donationHistory")) : null;
    }

}


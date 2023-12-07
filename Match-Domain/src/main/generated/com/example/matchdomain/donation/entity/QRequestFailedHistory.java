package com.example.matchdomain.donation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRequestFailedHistory is a Querydsl query type for RequestFailedHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRequestFailedHistory extends EntityPathBase<RequestFailedHistory> {

    private static final long serialVersionUID = -443267059L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRequestFailedHistory requestFailedHistory = new QRequestFailedHistory("requestFailedHistory");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath reason = createString("reason");

    public final QRegularPayment regularPayment;

    public final NumberPath<Long> regularPaymentId = createNumber("regularPaymentId", Long.class);

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRequestFailedHistory(String variable) {
        this(RequestFailedHistory.class, forVariable(variable), INITS);
    }

    public QRequestFailedHistory(Path<? extends RequestFailedHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRequestFailedHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRequestFailedHistory(PathMetadata metadata, PathInits inits) {
        this(RequestFailedHistory.class, metadata, inits);
    }

    public QRequestFailedHistory(Class<? extends RequestFailedHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.regularPayment = inits.isInitialized("regularPayment") ? new QRegularPayment(forProperty("regularPayment"), inits.get("regularPayment")) : null;
    }

}


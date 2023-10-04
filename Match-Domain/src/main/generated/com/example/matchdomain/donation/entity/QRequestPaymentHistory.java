package com.example.matchdomain.donation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.example.matchdomain.donation.entity.enums.PaymentStatus;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRequestPaymentHistory is a Querydsl query type for RequestPaymentHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRequestPaymentHistory extends EntityPathBase<RequestPaymentHistory> {

    private static final long serialVersionUID = 1915992056L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRequestPaymentHistory requestPaymentHistory = new QRequestPaymentHistory("requestPaymentHistory");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath orderId = createString("orderId");

    public final NumberPath<Integer> payDate = createNumber("payDate", Integer.class);

    public final EnumPath<PaymentStatus> paymentStatus = createEnum("paymentStatus", PaymentStatus.class);

    public final StringPath reason = createString("reason");

    public final QRegularPayment regularPayment;

    public final NumberPath<Long> regularPaymentId = createNumber("regularPaymentId", Long.class);

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    public final StringPath tid = createString("tid");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.matchdomain.user.entity.QUser user;

    public final QUserCard userCard;

    public final NumberPath<Long> userCardId = createNumber("userCardId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QRequestPaymentHistory(String variable) {
        this(RequestPaymentHistory.class, forVariable(variable), INITS);
    }

    public QRequestPaymentHistory(Path<? extends RequestPaymentHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRequestPaymentHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRequestPaymentHistory(PathMetadata metadata, PathInits inits) {
        this(RequestPaymentHistory.class, metadata, inits);
    }

    public QRequestPaymentHistory(Class<? extends RequestPaymentHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.regularPayment = inits.isInitialized("regularPayment") ? new QRegularPayment(forProperty("regularPayment"), inits.get("regularPayment")) : null;
        this.user = inits.isInitialized("user") ? new com.example.matchdomain.user.entity.QUser(forProperty("user")) : null;
        this.userCard = inits.isInitialized("userCard") ? new QUserCard(forProperty("userCard"), inits.get("userCard")) : null;
    }

}


package com.example.matchdomain.donation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserCard is a Querydsl query type for UserCard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserCard extends EntityPathBase<UserCard> {

    private static final long serialVersionUID = -1651946048L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserCard userCard = new QUserCard("userCard");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    public final StringPath bid = createString("bid");

    public final EnumPath<com.example.matchdomain.donation.entity.enums.CardAbleStatus> cardAbleStatus = createEnum("cardAbleStatus", com.example.matchdomain.donation.entity.enums.CardAbleStatus.class);

    public final EnumPath<com.example.matchdomain.donation.entity.enums.CardCode> cardCode = createEnum("cardCode", com.example.matchdomain.donation.entity.enums.CardCode.class);

    public final StringPath cardName = createString("cardName");

    public final StringPath cardNo = createString("cardNo");

    public final StringPath cardPw = createString("cardPw");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath customerId = createString("customerId");

    public final StringPath expMonth = createString("expMonth");

    public final StringPath expYear = createString("expYear");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath idNo = createString("idNo");

    public final StringPath orderId = createString("orderId");

    public final ListPath<RegularPayment, QRegularPayment> regularPayment = this.<RegularPayment, QRegularPayment>createList("regularPayment", RegularPayment.class, QRegularPayment.class, PathInits.DIRECT2);

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.matchdomain.user.entity.QUser user;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserCard(String variable) {
        this(UserCard.class, forVariable(variable), INITS);
    }

    public QUserCard(Path<? extends UserCard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserCard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserCard(PathMetadata metadata, PathInits inits) {
        this(UserCard.class, metadata, inits);
    }

    public QUserCard(Class<? extends UserCard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.matchdomain.user.entity.QUser(forProperty("user")) : null;
    }

}


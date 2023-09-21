package com.example.matchdomain.donationTemporary.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDonationTemporary is a Querydsl query type for DonationTemporary
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDonationTemporary extends EntityPathBase<DonationTemporary> {

    private static final long serialVersionUID = 1958221049L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDonationTemporary donationTemporary = new QDonationTemporary("donationTemporary");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    public final EnumPath<AlarmMethod> alarmMethod = createEnum("alarmMethod", AlarmMethod.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<Deposit> deposit = createEnum("deposit", Deposit.class);

    public final EnumPath<DonationKind> donationKind = createEnum("donationKind", DonationKind.class);

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath phoneNumber = createString("phoneNumber");

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.matchdomain.user.entity.QUser user;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QDonationTemporary(String variable) {
        this(DonationTemporary.class, forVariable(variable), INITS);
    }

    public QDonationTemporary(Path<? extends DonationTemporary> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDonationTemporary(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDonationTemporary(PathMetadata metadata, PathInits inits) {
        this(DonationTemporary.class, metadata, inits);
    }

    public QDonationTemporary(Class<? extends DonationTemporary> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.matchdomain.user.entity.QUser(forProperty("user")) : null;
    }

}


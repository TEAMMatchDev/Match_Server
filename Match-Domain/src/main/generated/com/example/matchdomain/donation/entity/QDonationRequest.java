package com.example.matchdomain.donation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDonationRequest is a Querydsl query type for DonationRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDonationRequest extends EntityPathBase<DonationRequest> {

    private static final long serialVersionUID = -485129544L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDonationRequest donationRequest = new QDonationRequest("donationRequest");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.matchdomain.user.entity.QUser user;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QDonationRequest(String variable) {
        this(DonationRequest.class, forVariable(variable), INITS);
    }

    public QDonationRequest(Path<? extends DonationRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDonationRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDonationRequest(PathMetadata metadata, PathInits inits) {
        this(DonationRequest.class, metadata, inits);
    }

    public QDonationRequest(Class<? extends DonationRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.matchdomain.user.entity.QUser(forProperty("user")) : null;
    }

}


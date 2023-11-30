package com.example.matchdomain.donation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDonationExecution is a Querydsl query type for DonationExecution
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDonationExecution extends EntityPathBase<DonationExecution> {

    private static final long serialVersionUID = 157037825L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDonationExecution donationExecution = new QDonationExecution("donationExecution");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QDonationUser donationUser;

    public final NumberPath<Long> donationUserId = createNumber("donationUserId", Long.class);

    public final EnumPath<com.example.matchdomain.donation.entity.enums.Execution> execution = createEnum("execution", com.example.matchdomain.donation.entity.enums.Execution.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final com.example.matchdomain.review.entity.QReview review;

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDonationExecution(String variable) {
        this(DonationExecution.class, forVariable(variable), INITS);
    }

    public QDonationExecution(Path<? extends DonationExecution> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDonationExecution(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDonationExecution(PathMetadata metadata, PathInits inits) {
        this(DonationExecution.class, metadata, inits);
    }

    public QDonationExecution(Class<? extends DonationExecution> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.donationUser = inits.isInitialized("donationUser") ? new QDonationUser(forProperty("donationUser"), inits.get("donationUser")) : null;
        this.review = inits.isInitialized("review") ? new com.example.matchdomain.review.entity.QReview(forProperty("review"), inits.get("review")) : null;
    }

}


package com.example.matchdomain.donation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDonationUser is a Querydsl query type for DonationUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDonationUser extends EntityPathBase<DonationUser> {

    private static final long serialVersionUID = 1698264130L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDonationUser donationUser = new QDonationUser("donationUser");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<DonationExecution, QDonationExecution> donationExecutions = this.<DonationExecution, QDonationExecution>createList("donationExecutions", DonationExecution.class, QDonationExecution.class, PathInits.DIRECT2);

    public final ListPath<DonationHistory, QDonationHistory> donationHistories = this.<DonationHistory, QDonationHistory>createList("donationHistories", DonationHistory.class, QDonationHistory.class, PathInits.DIRECT2);

    public final EnumPath<com.example.matchdomain.donation.entity.enums.DonationStatus> donationStatus = createEnum("donationStatus", com.example.matchdomain.donation.entity.enums.DonationStatus.class);

    public final StringPath flameImage = createString("flameImage");

    public final EnumPath<com.example.matchdomain.donation.entity.flameEnum.FlameType> flameType = createEnum("flameType", com.example.matchdomain.donation.entity.flameEnum.FlameType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath inherenceName = createString("inherenceName");

    public final StringPath inherenceNumber = createString("inherenceNumber");

    public final StringPath orderId = createString("orderId");

    public final EnumPath<com.example.matchdomain.donation.entity.enums.PayMethod> payMethod = createEnum("payMethod", com.example.matchdomain.donation.entity.enums.PayMethod.class);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final com.example.matchdomain.project.entity.QProject project;

    public final NumberPath<Long> projectId = createNumber("projectId", Long.class);

    public final QRegularPayment regularPayment;

    public final NumberPath<Long> regularPaymentId = createNumber("regularPaymentId", Long.class);

    public final EnumPath<com.example.matchdomain.donation.entity.enums.RegularStatus> regularStatus = createEnum("regularStatus", com.example.matchdomain.donation.entity.enums.RegularStatus.class);

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    public final StringPath tid = createString("tid");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.matchdomain.user.entity.QUser user;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QDonationUser(String variable) {
        this(DonationUser.class, forVariable(variable), INITS);
    }

    public QDonationUser(Path<? extends DonationUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDonationUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDonationUser(PathMetadata metadata, PathInits inits) {
        this(DonationUser.class, metadata, inits);
    }

    public QDonationUser(Class<? extends DonationUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.project = inits.isInitialized("project") ? new com.example.matchdomain.project.entity.QProject(forProperty("project")) : null;
        this.regularPayment = inits.isInitialized("regularPayment") ? new QRegularPayment(forProperty("regularPayment"), inits.get("regularPayment")) : null;
        this.user = inits.isInitialized("user") ? new com.example.matchdomain.user.entity.QUser(forProperty("user")) : null;
    }

}


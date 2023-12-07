package com.example.matchdomain.donation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRegularPayment is a Querydsl query type for RegularPayment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRegularPayment extends EntityPathBase<RegularPayment> {

    private static final long serialVersionUID = 1186884431L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRegularPayment regularPayment = new QRegularPayment("regularPayment");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<DonationUser, QDonationUser> donationUser = this.<DonationUser, QDonationUser>createList("donationUser", DonationUser.class, QDonationUser.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> payDate = createNumber("payDate", Integer.class);

    public final com.example.matchdomain.project.entity.QProject project;

    public final NumberPath<Long> projectId = createNumber("projectId", Long.class);

    public final EnumPath<com.example.matchdomain.donation.entity.enums.RegularPayStatus> regularPayStatus = createEnum("regularPayStatus", com.example.matchdomain.donation.entity.enums.RegularPayStatus.class);

    public final ListPath<RequestFailedHistory, QRequestFailedHistory> requestFailedHistories = this.<RequestFailedHistory, QRequestFailedHistory>createList("requestFailedHistories", RequestFailedHistory.class, QRequestFailedHistory.class, PathInits.DIRECT2);

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.matchdomain.user.entity.QUser user;

    public final QUserCard userCard;

    public final NumberPath<Long> userCardId = createNumber("userCardId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QRegularPayment(String variable) {
        this(RegularPayment.class, forVariable(variable), INITS);
    }

    public QRegularPayment(Path<? extends RegularPayment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRegularPayment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRegularPayment(PathMetadata metadata, PathInits inits) {
        this(RegularPayment.class, metadata, inits);
    }

    public QRegularPayment(Class<? extends RegularPayment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.project = inits.isInitialized("project") ? new com.example.matchdomain.project.entity.QProject(forProperty("project")) : null;
        this.user = inits.isInitialized("user") ? new com.example.matchdomain.user.entity.QUser(forProperty("user")) : null;
        this.userCard = inits.isInitialized("userCard") ? new QUserCard(forProperty("userCard"), inits.get("userCard")) : null;
    }

}


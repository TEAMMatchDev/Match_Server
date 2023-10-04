package com.example.matchdomain.donation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.example.matchdomain.donation.entity.enums.HistoryStatus;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDonationHistory is a Querydsl query type for DonationHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDonationHistory extends EntityPathBase<DonationHistory> {

    private static final long serialVersionUID = -653888323L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDonationHistory donationHistory = new QDonationHistory("donationHistory");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    public final NumberPath<Integer> cnt = createNumber("cnt", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QDonationUser donationUser;

    public final NumberPath<Long> donationUserId = createNumber("donationUserId", Long.class);

    public final StringPath flameImage = createString("flameImage");

    public final ListPath<HistoryImage, QHistoryImage> historyImages = this.<HistoryImage, QHistoryImage>createList("historyImages", HistoryImage.class, QHistoryImage.class, PathInits.DIRECT2);

    public final EnumPath<HistoryStatus> historyStatus = createEnum("historyStatus", HistoryStatus.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.example.matchdomain.project.entity.QProject project;

    public final NumberPath<Long> projectId = createNumber("projectId", Long.class);

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDonationHistory(String variable) {
        this(DonationHistory.class, forVariable(variable), INITS);
    }

    public QDonationHistory(Path<? extends DonationHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDonationHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDonationHistory(PathMetadata metadata, PathInits inits) {
        this(DonationHistory.class, metadata, inits);
    }

    public QDonationHistory(Class<? extends DonationHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.donationUser = inits.isInitialized("donationUser") ? new QDonationUser(forProperty("donationUser"), inits.get("donationUser")) : null;
        this.project = inits.isInitialized("project") ? new com.example.matchdomain.project.entity.QProject(forProperty("project")) : null;
    }

}


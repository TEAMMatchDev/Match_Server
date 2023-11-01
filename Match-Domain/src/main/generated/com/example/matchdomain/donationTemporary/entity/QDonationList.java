package com.example.matchdomain.donationTemporary.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDonationList is a Querydsl query type for DonationList
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDonationList extends EntityPathBase<DonationList> {

    private static final long serialVersionUID = 1849790710L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDonationList donationList = new QDonationList("donationList");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QDonationTemporary donationTemporary;

    public final NumberPath<Long> donationTemporaryId = createNumber("donationTemporaryId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QDonationList(String variable) {
        this(DonationList.class, forVariable(variable), INITS);
    }

    public QDonationList(Path<? extends DonationList> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDonationList(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDonationList(PathMetadata metadata, PathInits inits) {
        this(DonationList.class, metadata, inits);
    }

    public QDonationList(Class<? extends DonationList> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.donationTemporary = inits.isInitialized("donationTemporary") ? new QDonationTemporary(forProperty("donationTemporary"), inits.get("donationTemporary")) : null;
    }

}


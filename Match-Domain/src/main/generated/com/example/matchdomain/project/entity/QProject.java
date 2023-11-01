package com.example.matchdomain.project.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProject is a Querydsl query type for Project
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProject extends EntityPathBase<Project> {

    private static final long serialVersionUID = -261241415L;

    public static final QProject project = new QProject("project");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final ListPath<com.example.matchdomain.donation.entity.DonationUser, com.example.matchdomain.donation.entity.QDonationUser> donationUser = this.<com.example.matchdomain.donation.entity.DonationUser, com.example.matchdomain.donation.entity.QDonationUser>createList("donationUser", com.example.matchdomain.donation.entity.DonationUser.class, com.example.matchdomain.donation.entity.QDonationUser.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> finishedAt = createDateTime("finishedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath projectExplanation = createString("projectExplanation");

    public final ListPath<ProjectImage, QProjectImage> projectImage = this.<ProjectImage, QProjectImage>createList("projectImage", ProjectImage.class, QProjectImage.class, PathInits.DIRECT2);

    public final EnumPath<com.example.matchdomain.project.entity.enums.ProjectKind> projectKind = createEnum("projectKind", com.example.matchdomain.project.entity.enums.ProjectKind.class);

    public final StringPath projectName = createString("projectName");

    public final EnumPath<com.example.matchdomain.project.entity.enums.ProjectStatus> projectStatus = createEnum("projectStatus", com.example.matchdomain.project.entity.enums.ProjectStatus.class);

    public final ListPath<com.example.matchdomain.donation.entity.RegularPayment, com.example.matchdomain.donation.entity.QRegularPayment> regularPayments = this.<com.example.matchdomain.donation.entity.RegularPayment, com.example.matchdomain.donation.entity.QRegularPayment>createList("regularPayments", com.example.matchdomain.donation.entity.RegularPayment.class, com.example.matchdomain.donation.entity.QRegularPayment.class, PathInits.DIRECT2);

    public final EnumPath<com.example.matchdomain.donation.entity.enums.RegularStatus> regularStatus = createEnum("regularStatus", com.example.matchdomain.donation.entity.enums.RegularStatus.class);

    public final StringPath searchKeyword = createString("searchKeyword");

    public final DateTimePath<java.time.LocalDateTime> startedAt = createDateTime("startedAt", java.time.LocalDateTime.class);

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    public final EnumPath<com.example.matchdomain.project.entity.enums.TodayStatus> todayStatus = createEnum("todayStatus", com.example.matchdomain.project.entity.enums.TodayStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath usages = createString("usages");

    public final NumberPath<Integer> viewCnt = createNumber("viewCnt", Integer.class);

    public QProject(String variable) {
        super(Project.class, forVariable(variable));
    }

    public QProject(Path<? extends Project> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProject(PathMetadata metadata) {
        super(Project.class, metadata);
    }

}


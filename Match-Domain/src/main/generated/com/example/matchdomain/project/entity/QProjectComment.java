package com.example.matchdomain.project.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProjectComment is a Querydsl query type for ProjectComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProjectComment extends EntityPathBase<ProjectComment> {

    private static final long serialVersionUID = 538087014L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProjectComment projectComment = new QProjectComment("projectComment");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    public final StringPath comment = createString("comment");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QProject project;

    public final NumberPath<Long> projectId = createNumber("projectId", Long.class);

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.matchdomain.user.entity.QUser user;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QProjectComment(String variable) {
        this(ProjectComment.class, forVariable(variable), INITS);
    }

    public QProjectComment(Path<? extends ProjectComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProjectComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProjectComment(PathMetadata metadata, PathInits inits) {
        this(ProjectComment.class, metadata, inits);
    }

    public QProjectComment(Class<? extends ProjectComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.project = inits.isInitialized("project") ? new QProject(forProperty("project")) : null;
        this.user = inits.isInitialized("user") ? new com.example.matchdomain.user.entity.QUser(forProperty("user")) : null;
    }

}


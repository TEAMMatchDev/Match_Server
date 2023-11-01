package com.example.matchdomain.project.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProjectUserAttention is a Querydsl query type for ProjectUserAttention
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProjectUserAttention extends EntityPathBase<ProjectUserAttention> {

    private static final long serialVersionUID = 1572317274L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProjectUserAttention projectUserAttention = new QProjectUserAttention("projectUserAttention");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.example.matchdomain.project.entity.pk.QProjectUserAttentionPk id;

    public final QProject project;

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.matchdomain.user.entity.QUser user;

    public QProjectUserAttention(String variable) {
        this(ProjectUserAttention.class, forVariable(variable), INITS);
    }

    public QProjectUserAttention(Path<? extends ProjectUserAttention> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProjectUserAttention(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProjectUserAttention(PathMetadata metadata, PathInits inits) {
        this(ProjectUserAttention.class, metadata, inits);
    }

    public QProjectUserAttention(Class<? extends ProjectUserAttention> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new com.example.matchdomain.project.entity.pk.QProjectUserAttentionPk(forProperty("id")) : null;
        this.project = inits.isInitialized("project") ? new QProject(forProperty("project")) : null;
        this.user = inits.isInitialized("user") ? new com.example.matchdomain.user.entity.QUser(forProperty("user")) : null;
    }

}


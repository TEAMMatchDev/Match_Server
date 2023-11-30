package com.example.matchdomain.common.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QContentsEntity is a Querydsl query type for ContentsEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QContentsEntity extends EntityPathBase<ContentsEntity> {

    private static final long serialVersionUID = 2070380719L;

    public static final QContentsEntity contentsEntity = new QContentsEntity("contentsEntity");

    public final StringPath contents = createString("contents");

    public final EnumPath<ContentsType> contentsType = createEnum("contentsType", ContentsType.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final EnumPath<Status> status = createEnum("status", Status.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QContentsEntity(String variable) {
        super(ContentsEntity.class, forVariable(variable));
    }

    public QContentsEntity(Path<? extends ContentsEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContentsEntity(PathMetadata metadata) {
        super(ContentsEntity.class, metadata);
    }

}


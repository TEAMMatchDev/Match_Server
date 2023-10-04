package com.example.matchdomain.project.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.example.matchdomain.project.entity.enums.ImageRepresentStatus;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProjectImage is a Querydsl query type for ProjectImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProjectImage extends EntityPathBase<ProjectImage> {

    private static final long serialVersionUID = -1236426974L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProjectImage projectImage = new QProjectImage("projectImage");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<ImageRepresentStatus> imageRepresentStatus = createEnum("imageRepresentStatus", ImageRepresentStatus.class);

    public final QProject project;

    public final NumberPath<Long> projectId = createNumber("projectId", Long.class);

    public final NumberPath<Integer> sequence = createNumber("sequence", Integer.class);

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath url = createString("url");

    public QProjectImage(String variable) {
        this(ProjectImage.class, forVariable(variable), INITS);
    }

    public QProjectImage(Path<? extends ProjectImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProjectImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProjectImage(PathMetadata metadata, PathInits inits) {
        this(ProjectImage.class, metadata, inits);
    }

    public QProjectImage(Class<? extends ProjectImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.project = inits.isInitialized("project") ? new QProject(forProperty("project")) : null;
    }

}


package com.example.matchdomain.project.entity.pk;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProjectUserAttentionPk is a Querydsl query type for ProjectUserAttentionPk
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QProjectUserAttentionPk extends BeanPath<ProjectUserAttentionPk> {

    private static final long serialVersionUID = -558275896L;

    public static final QProjectUserAttentionPk projectUserAttentionPk = new QProjectUserAttentionPk("projectUserAttentionPk");

    public final NumberPath<Long> projectId = createNumber("projectId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QProjectUserAttentionPk(String variable) {
        super(ProjectUserAttentionPk.class, forVariable(variable));
    }

    public QProjectUserAttentionPk(Path<? extends ProjectUserAttentionPk> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProjectUserAttentionPk(PathMetadata metadata) {
        super(ProjectUserAttentionPk.class, metadata);
    }

}


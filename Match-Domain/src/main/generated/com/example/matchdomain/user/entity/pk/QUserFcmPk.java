package com.example.matchdomain.user.entity.pk;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserFcmPk is a Querydsl query type for UserFcmPk
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUserFcmPk extends BeanPath<UserFcmPk> {

    private static final long serialVersionUID = 1206061675L;

    public static final QUserFcmPk userFcmPk = new QUserFcmPk("userFcmPk");

    public final StringPath fcmToken = createString("fcmToken");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserFcmPk(String variable) {
        super(UserFcmPk.class, forVariable(variable));
    }

    public QUserFcmPk(Path<? extends UserFcmPk> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserFcmPk(PathMetadata metadata) {
        super(UserFcmPk.class, metadata);
    }

}


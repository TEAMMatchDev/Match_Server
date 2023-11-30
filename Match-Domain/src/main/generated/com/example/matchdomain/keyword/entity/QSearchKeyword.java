package com.example.matchdomain.keyword.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSearchKeyword is a Querydsl query type for SearchKeyword
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSearchKeyword extends EntityPathBase<SearchKeyword> {

    private static final long serialVersionUID = -2088356623L;

    public static final QSearchKeyword searchKeyword = new QSearchKeyword("searchKeyword");

    public final com.example.matchdomain.common.model.QBaseEntity _super = new com.example.matchdomain.common.model.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keyword = createString("keyword");

    public final NumberPath<Integer> priority = createNumber("priority", Integer.class);

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSearchKeyword(String variable) {
        super(SearchKeyword.class, forVariable(variable));
    }

    public QSearchKeyword(Path<? extends SearchKeyword> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSearchKeyword(PathMetadata metadata) {
        super(SearchKeyword.class, metadata);
    }

}


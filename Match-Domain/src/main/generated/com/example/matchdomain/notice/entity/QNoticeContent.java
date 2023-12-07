package com.example.matchdomain.notice.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNoticeContent is a Querydsl query type for NoticeContent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNoticeContent extends EntityPathBase<NoticeContent> {

    private static final long serialVersionUID = -986976170L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNoticeContent noticeContent = new QNoticeContent("noticeContent");

    public final com.example.matchdomain.common.model.QContentsEntity _super = new com.example.matchdomain.common.model.QContentsEntity(this);

    //inherited
    public final StringPath contents = _super.contents;

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.ContentsType> contentsType = _super.contentsType;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QNotice notice;

    public final NumberPath<Long> noticeId = createNumber("noticeId", Long.class);

    //inherited
    public final EnumPath<com.example.matchdomain.common.model.Status> status = _super.status;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QNoticeContent(String variable) {
        this(NoticeContent.class, forVariable(variable), INITS);
    }

    public QNoticeContent(Path<? extends NoticeContent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNoticeContent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNoticeContent(PathMetadata metadata, PathInits inits) {
        this(NoticeContent.class, metadata, inits);
    }

    public QNoticeContent(Class<? extends NoticeContent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.notice = inits.isInitialized("notice") ? new QNotice(forProperty("notice")) : null;
    }

}


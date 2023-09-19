package com.example.matchdomain.project.repository;

import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.QRegularPayment;
import com.example.matchdomain.project.dto.ProjectList;
import com.example.matchdomain.project.entity.*;
import com.example.matchdomain.user.entity.QUser;
import com.example.matchdomain.user.entity.User;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
public class ProjectCustomRepositoryImpl implements ProjectCustomRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProjectList> searchProjectCustom(User users, int page, int size, ProjectKind kind, String content, ProjectStatus projectStatus, LocalDateTime now, ImageRepresentStatus imageRepresentStatus, Status status, Pageable pageable) {

        QProject project = QProject.project;
        QProjectImage projectImage = QProjectImage.projectImage;
        QRegularPayment regularPayment = QRegularPayment.regularPayment;
        QUser user = QUser.user;
        QProjectUserAttention projectUserAttention = QProjectUserAttention.projectUserAttention;

        Predicate predicate = buildSearchPredicate(project, projectImage, kind, content, imageRepresentStatus, projectStatus , now, status);

        JPAQuery<ProjectList> query = queryFactory
                .select(
                        Projections.constructor(
                                ProjectList.class,
                                project.id,
                                project.usages,
                                project.projectKind,
                                project.projectName,
                                project.searchKeyword,
                                project.projectExplanation,
                                project.status,
                                project.finishedAt,
                                project.projectStatus,
                                project.viewCnt,
                                projectImage.url,
                                JPAExpressions
                                        .selectOne()
                                        .from(projectUserAttention)
                                        .where(projectUserAttention.user.eq(users).and(projectUserAttention.project.eq(project)))
                                        .exists(),
                                JPAExpressions
                                        .select(regularPayment.count())
                                        .from(regularPayment)
                                        .where(regularPayment.projectId.eq(project.id))
                        )
                )
                .from(project)
                .leftJoin(projectImage).on(project.id.eq(projectImage.projectId))
                .where(
                        predicate
                )
                .groupBy(project.id);

        List<ProjectList> results = query.fetch();

        long total = query.fetchCount();

        for (int i = 0; i < results.size(); i++) {
            results.get(i).setImgUrlList(getImgUrlList(results.get(i).getId()));
        }

        return new PageImpl<>(results, pageable, total);
    }

    private List<String> getImgUrlList(Long projectId){
        QRegularPayment regularPayment = QRegularPayment.regularPayment;
        QUser user = QUser.user;

        return queryFactory
                .select(user.profileImgUrl)
                .from(regularPayment)
                .join(user).on(regularPayment.userId.eq(user.id))
                .where(regularPayment.projectId.eq(projectId))
                .distinct()
                .limit(3)
                .fetch();
    }

    private Predicate buildSearchPredicate(QProject P, QProjectImage PI, ProjectKind kind, String content, ImageRepresentStatus imageRepresentStatus, ProjectStatus projectStatus, LocalDateTime now, Status status) {
        BooleanExpression predicate = PI.imageRepresentStatus.eq(imageRepresentStatus)
                .and(P.projectStatus.eq(projectStatus))
                .and(P.finishedAt.goe(now))
                .and(P.status.eq(status));

        if (kind != null) {
            predicate = predicate.and(P.projectKind.eq(kind));
        }

        if (content != null && !content.isEmpty()) {
            predicate = predicate.andAnyOf(
                    P.projectName.like("%" + content + "%"),
                    P.projectExplanation.like("%" + content + "%"),
                    P.usages.like("%" + content + "%"),
                    P.searchKeyword.like("%" + content + "%")
            );
        }

        return predicate;
    }
}

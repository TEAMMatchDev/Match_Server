package com.example.matchdomain.project.repository;

import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.QRegularPayment;
import com.example.matchdomain.project.dto.ProjectDto;
import com.example.matchdomain.project.dto.ProjectList;
import com.example.matchdomain.project.entity.*;
import com.example.matchdomain.project.entity.enums.ImageRepresentStatus;
import com.example.matchdomain.project.entity.enums.ProjectKind;
import com.example.matchdomain.project.entity.enums.ProjectStatus;
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
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.matchdomain.donation.entity.enums.RegularPayStatus.PROCEEDING;


@RequiredArgsConstructor
public class ProjectCustomRepositoryImpl implements ProjectCustomRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProjectList> searchProjectCustom(User users, int page, int size, ProjectKind kind, String content, ProjectStatus projectStatus, LocalDateTime now, ImageRepresentStatus imageRepresentStatus, Status status, Pageable pageable) {

        QProject project = QProject.project;
        QProjectImage projectImage = QProjectImage.projectImage;
        QRegularPayment regularPayment = QRegularPayment.regularPayment;
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

        for (ProjectList result : results) {
            result.setImgUrlList(getImgUrlList(result.getId()));
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

    @Override
    public Page<ProjectList> getTodayProjectCustom(User user, int page, int size, ProjectStatus projectStatus, LocalDateTime now, ImageRepresentStatus imageRepresentStatus, Status status) {
        QProject project = QProject.project;


        return null;
    }

    @Override
    public Page<Project> getProjectList(User user, ProjectStatus projectStatus, LocalDateTime now, ImageRepresentStatus imageRepresentStatus, Status status, ProjectKind projectKind, String content, Pageable pageable) {
        QProject qproject = QProject.project;
        QProjectImage qProjectImage = QProjectImage.projectImage;
        QUser qUser = QUser.user;
        QRegularPayment qRegularPayment = QRegularPayment.regularPayment;

        Predicate predicate = buildSearchPredicate(qproject, qProjectImage, projectKind, content, imageRepresentStatus, projectStatus , now, status);

        List<Project> projects = queryFactory
                .select(qproject)
                .from(qproject)
                .join(qProjectImage).on(qproject.eq(qProjectImage.project).and(qProjectImage.imageRepresentStatus.eq(imageRepresentStatus))).fetchJoin()
                .leftJoin(qRegularPayment).on(qRegularPayment.project.eq(qproject).and(qRegularPayment.regularPayStatus.eq(PROCEEDING))).fetchJoin()
                .join(qUser).on(qUser.eq(qRegularPayment.user)).fetchJoin()
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Project> countQuery = queryFactory.selectFrom(qproject).join(qProjectImage).on(qproject.eq(qProjectImage.project).and(qProjectImage.imageRepresentStatus.eq(imageRepresentStatus))).fetchJoin()
                .leftJoin(qRegularPayment).on(qRegularPayment.project.eq(qproject).and(qRegularPayment.regularPayStatus.eq(PROCEEDING))).fetchJoin()
                .join(qUser).on(qUser.eq(qRegularPayment.user)).fetchJoin()
                .where(predicate);
        return PageableExecutionUtils.getPage(projects, pageable, countQuery::fetchCount);

    }

    public Page<ProjectDto> findProject(User user, ProjectStatus projectStatus, LocalDateTime now, ImageRepresentStatus imageRepresentStatus, Status status, ProjectKind projectKind, String content, Pageable pageable) {
        QProject project = QProject.project;
        QProjectImage projectImage = QProjectImage.projectImage;
        QRegularPayment regularPayment = QRegularPayment.regularPayment;
        QProjectUserAttention projectUserAttention = QProjectUserAttention.projectUserAttention;
        QUser qUser = QUser.user;

        Predicate predicate = buildSearchPredicate(project, projectImage, projectKind, content, imageRepresentStatus, projectStatus , now, status);

        List<ProjectDto> projectDtos = queryFactory
                .select(
                        Projections.fields(
                                ProjectDto.class,
                                project.id.as("id"),
                                project.usages.as("usages"),
                                project.projectKind.as("projectKind"),
                                project.viewCnt,
                                project.projectName.as("projectName"),
                                projectImage.url.as("imgUrl"),
                                JPAExpressions
                                        .select(projectUserAttention.user.eq(user))
                                        .from(projectUserAttention)
                                        .where(projectUserAttention.project.eq(project))
                                        .exists()
                                        .as("like")
                        )
                )
                .from(project)
                .join(projectImage)
                .on(project.id.eq(projectImage.projectId)).fetchJoin()
                .leftJoin(regularPayment)
                .on(
                        regularPayment.projectId.eq(project.id),
                        regularPayment.regularPayStatus.eq(PROCEEDING)
                ).fetchJoin()
                .where(
                        predicate
                )
                .groupBy(project.id)
                .orderBy(regularPayment.id.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Project> countQuery = queryFactory.selectFrom(project)
                .where(predicate);

        return PageableExecutionUtils.getPage(projectDtos, pageable, countQuery::fetchCount);
    }

    @Override
    public long countQueryForProject(ProjectStatus projectStatus, LocalDateTime now, Status status, String content, ProjectKind projectKind) {
        QProject project = QProject.project;

        Predicate predicate = buildSearchCountPredicate(projectStatus, now, status, content, projectKind, project);

        return queryFactory.selectFrom(project)
                .where(predicate)
                .fetchCount();
    }

    private Predicate buildSearchCountPredicate(ProjectStatus projectStatus, LocalDateTime now, Status status, String content, ProjectKind projectKind, QProject project) {
        BooleanExpression predicate = project.projectStatus.eq(projectStatus)
                .and(project.finishedAt.goe(now))
                .and(project.status.eq(status));

        if (projectKind != null) {
            predicate = predicate.and(project.projectKind.eq(projectKind));
        }
        if (content != null) {
            predicate = predicate.and(project.projectName.like("%" + content + "%")
                    .or(project.projectExplanation.like("%" + content + "%"))
                    .or(project.usages.like("%" + content + "%"))
                    .or(project.searchKeyword.like("%" + content + "%")));
        }


        return predicate;
    }

}

package com.example.matchdomain.project.repository;

import com.example.matchdomain.project.entity.ImageRepresentStatus;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @EntityGraph(attributePaths = "projectImage")
    Page<Project> findByProjectStatusAndProjectImage_ImageRepresentStatusOrderByViewCnt(ProjectStatus projectStatus, ImageRepresentStatus imageRepresentStatus, Pageable pageable);

    @EntityGraph(attributePaths = "projectImage")
    Page<Project> findByProjectStatusOrProjectNameContainingOrUsagesContainingOrProjectExplanationContainingAndProjectImage_ImageRepresentStatusOrderByViewCnt(ProjectStatus projectStatus, String content, String s, String content1,  ImageRepresentStatus imageRepresentStatus, Pageable pageable);
    @EntityGraph(attributePaths = "projectImage")
    Page<Project> findByProjectStatusAndFinishedAtGreaterThanEqualAndProjectImage_ImageRepresentStatusOrderByViewCnt(ProjectStatus projectStatus, LocalDateTime now, ImageRepresentStatus imageRepresentStatus, Pageable pageable);

    @EntityGraph(attributePaths = "projectImage")
    @Query("SELECT p FROM Project p " +
            "join ProjectImage pi on p.id = pi.projectId " +
            "WHERE " +
            "(p.projectName LIKE %:content% OR p.projectExplanation LIKE %:content1% OR p.usages LIKE %:content2%) " +
            "AND p.projectStatus = :projectStatus " +
            "AND p.finishedAt >= :now " +
            "AND pi.imageRepresentStatus = :imageRepresentStatus " +
            "ORDER BY p.viewCnt ASC")
    Page<Project> searchProject(@Param("content") String content,@Param("content1")  String content1,@Param("content2") String content2, @Param("projectStatus")  ProjectStatus projectStatus, @Param("now") LocalDateTime now, @Param("imageRepresentStatus") ImageRepresentStatus imageRepresentStatus, Pageable pageable);
    @EntityGraph(attributePaths = "projectImage")
    Page<Project> findByProjectNameContainingOrUsagesContainingOrProjectExplanationContainingAndProjectStatusAndFinishedAtGreaterThanAndProjectImage_ImageRepresentStatusOrderByViewCnt(String content, String content1, String content2, ProjectStatus projectStatus, LocalDateTime now, ImageRepresentStatus imageRepresentStatus, Pageable pageable);
}

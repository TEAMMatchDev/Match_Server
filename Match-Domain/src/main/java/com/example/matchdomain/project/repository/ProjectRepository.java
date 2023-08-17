package com.example.matchdomain.project.repository;

import com.example.matchdomain.project.entity.ImageRepresentStatus;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.entity.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @EntityGraph(attributePaths = "projectImage")
    Page<Project> findByProjectStatusAndProjectImage_ImageRepresentStatusOrderByViewCnt(ProjectStatus projectStatus, ImageRepresentStatus imageRepresentStatus, Pageable pageable);

    @EntityGraph(attributePaths = "projectImage")
    Page<Project> findByProjectStatusOrProjectNameContainingOrUsagesContainingOrProjectExplanationContainingAndProjectImage_ImageRepresentStatusOrderByViewCnt(ProjectStatus projectStatus, String content, String s, String content1,  ImageRepresentStatus imageRepresentStatus, Pageable pageable);
}

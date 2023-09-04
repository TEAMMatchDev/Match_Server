package com.example.matchdomain.project.repository;

import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.project.entity.ImageRepresentStatus;
import com.example.matchdomain.project.entity.ProjectImage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {
    @EntityGraph(attributePaths = "project")
    List<ProjectImage> findByProjectIdAndImageRepresentStatusOrderBySequenceAsc(Long projectId, ImageRepresentStatus imageRepresentStatus);

    @EntityGraph(attributePaths = "project")
    List<ProjectImage> findByProjectIdAndImageRepresentStatusAndProject_StatusOrderBySequenceAsc(Long projectId, ImageRepresentStatus imageRepresentStatus, Status status);

    List<ProjectImage> findByProjectIdOrderBySequenceAsc(Long projectId);
}

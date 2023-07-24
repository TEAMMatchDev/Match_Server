package com.example.matchdomain.project.repository;

import com.example.matchdomain.project.entity.ProjectImage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {
    @EntityGraph(attributePaths = "project")
    List<ProjectImage> findByProjectId(Long projectId);
}

package com.example.matchdomain.project.repository;

import com.example.matchdomain.project.entity.ImageRepresentStatus;
import com.example.matchdomain.project.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @EntityGraph(attributePaths = "projectImage")
    Page<Project> findByProjectImage_ImageRepresentStatusOrderByViewCnt(ImageRepresentStatus imageRepresentStatus, Pageable pageable);

}

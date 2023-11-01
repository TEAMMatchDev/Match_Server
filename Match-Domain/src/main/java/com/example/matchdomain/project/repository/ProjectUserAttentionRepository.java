package com.example.matchdomain.project.repository;

import com.example.matchdomain.project.entity.enums.ImageRepresentStatus;
import com.example.matchdomain.project.entity.ProjectUserAttention;
import com.example.matchdomain.project.entity.pk.ProjectUserAttentionPk;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectUserAttentionRepository extends JpaRepository<ProjectUserAttention, ProjectUserAttentionPk> {


    @EntityGraph(attributePaths = {"user", "project", "project.projectImage"})
    List<ProjectUserAttention> findById_userIdAndProject_ProjectImage_imageRepresentStatusOrderByCreatedAt(
            @Param("userId") Long userId,
            @Param("imageRepresentStatus") ImageRepresentStatus imageRepresentStatus
    );

    boolean existsById_userIdAndId_projectId(Long id, Long projectId);

    void deleteById_userIdAndId_projectId(Long id, Long projectId);

    List<ProjectUserAttention> findById_userId(Long id);

    Long countById_userId(Long id);
}

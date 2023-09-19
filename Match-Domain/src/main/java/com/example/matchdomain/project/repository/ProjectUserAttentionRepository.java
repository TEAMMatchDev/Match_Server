package com.example.matchdomain.project.repository;

import com.example.matchdomain.project.entity.ImageRepresentStatus;
import com.example.matchdomain.project.entity.ProjectImage;
import com.example.matchdomain.project.entity.ProjectUserAttention;
import com.example.matchdomain.project.entity.pk.ProjectUserAttentionPk;
import com.example.matchdomain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}

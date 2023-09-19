package com.example.matchdomain.project.repository;

import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.project.entity.ImageRepresentStatus;
import com.example.matchdomain.project.entity.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {
    @Query(value = "SELECT PI FROM ProjectImage PI JOIN FETCH PI.project " +
            "WHERE PI.projectId = :projectId " +
            "AND PI.imageRepresentStatus=:imageRepresentStatus " +
            "AND PI.project.status = :status " +
            "ORDER BY PI.sequence ASC")
    List<ProjectImage> findByProjectIdAndImageRepresentStatusAndProject_StatusOrderBySequenceAsc(@Param("projectId") Long projectId, @Param("imageRepresentStatus") ImageRepresentStatus imageRepresentStatus,@Param("status") Status status);

    List<ProjectImage> findByProjectIdOrderBySequenceAsc(Long projectId);
}

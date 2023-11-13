package com.example.matchdomain.project.repository;

import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.project.entity.ProjectComment;
import com.example.matchdomain.project.entity.pk.ProjectUserAttentionPk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectCommentRepository extends JpaRepository<ProjectComment, ProjectUserAttentionPk> {


    Page<ProjectComment> findByProjectIdOrderByCreatedAtDesc(Long projectId, Pageable pageable);

    Page<ProjectComment> findByProjectIdAndStatusOrderByCreatedAtAsc(Long projectId, Status status, Pageable pageable);

    Optional<ProjectComment> findByIdAndStatus(Long commentId, Status status);

    void deleteByUserId(Long id);
}

package com.example.matchdomain.project.repository;

import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.project.dto.ProjectDto;
import com.example.matchdomain.project.dto.ProjectList;
import com.example.matchdomain.project.entity.ImageRepresentStatus;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.entity.ProjectKind;
import com.example.matchdomain.project.entity.ProjectStatus;
import com.example.matchdomain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ProjectCustomRepository {
    Page<ProjectList> searchProjectCustom(User user, int page, int size, ProjectKind projectKind, String content, ProjectStatus proceeding, LocalDateTime now, ImageRepresentStatus represent, Status active, Pageable pageable);

    Page<ProjectList> getTodayProjectCustom(User user, int page, int size, ProjectStatus projectStatus, LocalDateTime now, ImageRepresentStatus imageRepresentStatus, Status status);

    Page<Project> getProjectList(User user, ProjectStatus projectStatus, LocalDateTime now, ImageRepresentStatus imageRepresentStatus, Status status, ProjectKind projectKind, String content, Pageable pageable);

    Page<ProjectDto> findProject(User user, ProjectStatus projectStatus, LocalDateTime now, ImageRepresentStatus imageRepresentStatus, Status status, ProjectKind projectKind, String content, Pageable pageable);
}

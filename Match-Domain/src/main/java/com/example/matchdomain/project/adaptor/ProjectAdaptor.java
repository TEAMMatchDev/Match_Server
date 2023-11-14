package com.example.matchdomain.project.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.constants.enums.FILTER;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.enums.RegularStatus;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.entity.enums.ImageRepresentStatus;
import com.example.matchdomain.project.entity.enums.ProjectKind;
import com.example.matchdomain.project.exception.ProjectOneTimeErrorCode;
import com.example.matchdomain.project.repository.ProjectRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.matchdomain.common.model.Status.ACTIVE;
import static com.example.matchdomain.project.entity.enums.ImageRepresentStatus.REPRESENT;
import static com.example.matchdomain.project.entity.enums.ProjectStatus.PROCEEDING;
import static com.example.matchdomain.project.entity.enums.TodayStatus.TODAY;
import static com.example.matchdomain.project.exception.ProjectGetErrorCode.PROJECT_NOT_EXIST;

@Adaptor
@RequiredArgsConstructor
public class ProjectAdaptor {
    private final ProjectRepository projectRepository;

    public Page<ProjectRepository.ProjectList> findLoginUserProjectList(Long userId, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        return projectRepository.findLoginUserProjectList(userId, PROCEEDING.getValue(), LocalDateTime.now(), ImageRepresentStatus.REPRESENT.getValue(), pageable, ACTIVE.getValue());
    }
    public Page<Project> findNotLoginUserProjectList(int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        return projectRepository.findByStatusAndProjectStatusAndFinishedAtGreaterThanEqualAndProjectImage_ImageRepresentStatusOrderByViewCnt(ACTIVE, PROCEEDING, LocalDateTime.now(), ImageRepresentStatus.REPRESENT, pageable);
    }

    public Page<ProjectRepository.ProjectList> findLoginUserSearchProjectList(Long userId, int page, int size, String content){
        Pageable pageable = PageRequest.of(page, size);

        return projectRepository.searchProjectLoginUser(userId,content,content,content, PROCEEDING.getValue(),LocalDateTime.now(), ImageRepresentStatus.REPRESENT.getValue(),pageable, ACTIVE.getValue());
    }
    public Page<Project> findNotLoginUserSearchProjectList(String content, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        return projectRepository.searchProject(content,content,content, PROCEEDING,LocalDateTime.now(), ImageRepresentStatus.REPRESENT,pageable, ACTIVE);

    }

    public ProjectRepository.ProjectAdminDetail projectAdminDetail(Long projectId) {
        return projectRepository.getProjectAdminDetail(projectId);
    }

    public Project findById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(()-> new NotFoundException(PROJECT_NOT_EXIST));
    }


    public Page<ProjectRepository.ProjectList> findProject(User user, int page, int size, ProjectKind projectKind, String content, FILTER filter) {
        Page<ProjectRepository.ProjectList> projects = null;
        Pageable pageable = PageRequest.of(page, size);

        if(filter == FILTER.RECOMMEND) {
            if (projectKind == null) {
                if (content == null) {
                    projects = projectRepository.findLoginUserProjectList(user.getId(), PROCEEDING.getValue(), LocalDateTime.now(), ImageRepresentStatus.REPRESENT.getValue(), pageable, ACTIVE.getValue());
                } else {
                    projects = projectRepository.findByContent(user.getId(), PROCEEDING.getValue(), LocalDateTime.now(), ImageRepresentStatus.REPRESENT.getValue(), pageable, ACTIVE.getValue(), content);

                }
            } else {
                if (content == null) {
                    projects = projectRepository.findByProjectKind(user.getId(), PROCEEDING.getValue(), LocalDateTime.now(),
                            ImageRepresentStatus.REPRESENT.getValue(), pageable, ACTIVE.getValue(), projectKind.getValue());

                } else {
                    projects = projectRepository.findByContentAndProjectKind(user.getId(), PROCEEDING.getValue(), LocalDateTime.now(),
                            ImageRepresentStatus.REPRESENT.getValue(), pageable, ACTIVE.getValue(), projectKind.getValue(), content);
                }
            }
        }else{
            if (projectKind == null) {
                if (content == null) {
                    projects = projectRepository.findLoginUserProjectListLatest(user.getId(), PROCEEDING.getValue(), LocalDateTime.now(), ImageRepresentStatus.REPRESENT.getValue(), pageable, ACTIVE.getValue());
                } else {
                    projects = projectRepository.findByContentLatest(user.getId(), PROCEEDING.getValue(), LocalDateTime.now(), ImageRepresentStatus.REPRESENT.getValue(), pageable, ACTIVE.getValue(), content);

                }
            } else {
                if (content == null) {
                    projects = projectRepository.findByProjectKindLatest(user.getId(), PROCEEDING.getValue(), LocalDateTime.now(),
                            ImageRepresentStatus.REPRESENT.getValue(), pageable, ACTIVE.getValue(), projectKind.getValue());

                } else {
                    projects = projectRepository.findByContentAndProjectKindLatest(user.getId(), PROCEEDING.getValue(), LocalDateTime.now(),
                            ImageRepresentStatus.REPRESENT.getValue(), pageable, ACTIVE.getValue(), projectKind.getValue(), content);
                }
            }
        }

        return projects;
    }

    public Page<ProjectRepository.ProjectList> getTodayProjectLists(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return projectRepository.findTodayProject(userId, PROCEEDING.getValue(), LocalDateTime.now(), REPRESENT.getValue(), ACTIVE.getValue(), TODAY.getValue(), pageable);
    }

    public Project checkRegularProjects(Long projectId, RegularStatus regularStatus) {
        return projectRepository.findByIdAndStatusAndRegularStatus(projectId, Status.ACTIVE, regularStatus).orElseThrow(() -> new BadRequestException(ProjectOneTimeErrorCode.PROJECT_NOT_EXIST));
    }

    public Optional<Project> findByProjectId(Long projectId) {
        return projectRepository.findById(projectId);
    }

    public Page<ProjectRepository.ProjectList> findLikeProjects(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ProjectRepository.ProjectList> projects = projectRepository.findLikeProjects(user.getId(), pageable);
        return projects;
    }

    public Project findByProject(String projectId) {
        return findByProjectId(Long.valueOf(projectId)).orElseThrow(()->new BadRequestException(ProjectOneTimeErrorCode.PROJECT_NOT_EXIST));
    }
}

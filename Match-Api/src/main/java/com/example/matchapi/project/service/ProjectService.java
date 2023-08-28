package com.example.matchapi.project.service;

import com.example.matchapi.project.convertor.ProjectConvertor;
import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchapi.user.helper.AuthHelper;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.project.entity.*;
import com.example.matchdomain.project.repository.ProjectCommentRepository;
import com.example.matchdomain.project.repository.ProjectImageRepository;
import com.example.matchdomain.project.repository.ProjectRepository;
import com.example.matchdomain.project.repository.ProjectUserAttentionRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectConvertor projectConvertor;
    private final ProjectImageRepository projectImageRepository;
    private final AuthHelper authHelper;
    private final ProjectUserAttentionRepository projectUserAttentionRepository;
    private final ProjectCommentRepository projectCommentRepository;

    public PageResponse<List<ProjectRes.ProjectList>> getProjectList(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Long userId = 0L;
        if(authHelper.checkGuest(user)) userId = user.getId();


        List<ProjectRes.ProjectList> projectLists = new ArrayList<>();

        if(!userId.equals(0L)){
            Page<ProjectRepository.ProjectList> projects = projectRepository.findLoginUserProjectList(userId, ProjectStatus.PROCEEDING.getValue(), LocalDateTime.now(), ImageRepresentStatus.REPRESENT.getValue(), pageable);
            projects.getContent().forEach(
                    result -> {
                        projectLists.add(new ProjectRes.ProjectList(
                                result.getId(),
                                result.getImgUrl(),
                                result.getProjectName(),
                                result.getUsages(),
                                result.getProjectKind(),
                                result.getLike()
                        ));
                    }
            );
            return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectLists);
        }

        else{
            Page<Project> projects = projectRepository.findByProjectStatusAndFinishedAtGreaterThanEqualAndProjectImage_ImageRepresentStatusOrderByViewCnt(ProjectStatus.PROCEEDING, LocalDateTime.now(), ImageRepresentStatus.REPRESENT, pageable);
            projects.getContent().forEach(
                    result -> {
                        String imageUrl = result.getProjectImage().isEmpty() ? null : result.getProjectImage().get(0).getUrl();
                        projectLists.add(new ProjectRes.ProjectList(
                                result.getId(),
                                imageUrl,
                                result.getProjectName(),
                                result.getUsages(),
                                result.getProjectKind().getValue(),
                                false
                        ));
                    }
            );
            return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectLists);
        }

    }

    public ProjectRes.ProjectDetail getProjectDetail(User user, Long projectId) {
        List<ProjectImage> projectImage = projectImageRepository.findByProjectIdAndImageRepresentStatusOrderBySequenceAsc(projectId, ImageRepresentStatus.NORMAL);
        return projectConvertor.projectImgList(projectImage);
    }

    public PageResponse<List<ProjectRes.ProjectList>> searchProjectList(User user, String content, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);


        List<ProjectRes.ProjectList> projectLists = new ArrayList<>();

        Long userId = 0L;
        if(authHelper.checkGuest(user)) userId = user.getId();

        if(!userId.equals(0L)){

            Page<ProjectRepository.ProjectList> projects = projectRepository.searchProjectLoginUser(userId,content,content,content,ProjectStatus.PROCEEDING.getValue(),LocalDateTime.now(), ImageRepresentStatus.REPRESENT.getValue(),pageable);

            projects.getContent().forEach(
                    result -> {
                        projectLists.add(new ProjectRes.ProjectList(
                                result.getId(),
                                result.getImgUrl(),
                                result.getProjectName(),
                                result.getUsages(),
                                result.getProjectKind(),
                                result.getLike()
                        ));
                    }
            );


            return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectLists);

        }
        else{
            Page<Project> projects = projectRepository.searchProject(content,content,content,ProjectStatus.PROCEEDING,LocalDateTime.now(),ImageRepresentStatus.REPRESENT,pageable);

            projects.getContent().forEach(
                    result -> {
                        String imageUrl = result.getProjectImage().isEmpty() ? null : result.getProjectImage().get(0).getUrl();
                        projectLists.add(new ProjectRes.ProjectList(
                                result.getId(),
                                imageUrl,
                                result.getProjectName(),
                                result.getUsages(),
                                result.getProjectKind().getValue(),
                                false
                        ));
                    }
            );


            return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectLists);
        }

    }

    public PageResponse<List<ProjectRes.CommentList>> getProjectComment(User user, Long projectId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Long userId;
        if(authHelper.checkGuest(user)) userId = user.getId();
        else {
            userId = 0L;
        }

        Page<ProjectComment> projectComments = projectCommentRepository.findByProjectIdOrderByCreatedAtDesc(projectId,pageable);

        List<ProjectRes.CommentList> commentLists = new ArrayList<>();
        projectComments.getContent().forEach(
                result-> {
                    commentLists.add(
                            projectConvertor.projectComment(userId, result)
                    );
                }
        );


        return null;
    }
}

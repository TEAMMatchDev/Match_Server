package com.example.matchapi.project.service;

import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.project.dto.ProjectDto;
import com.example.matchdomain.project.entity.ImageRepresentStatus;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.entity.RegularStatus;
import com.example.matchdomain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    public PageResponse<List<ProjectRes.ProjectList>> getProjectList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Project> projects = projectRepository.findByProjectImage_ImageRepresentStatusOrderByViewCnt(ImageRepresentStatus.REPRESENT,pageable);

        List<ProjectRes.ProjectList> projectLists = new ArrayList<>();

        projects.getContent().forEach(
                result -> {
                    String imageUrl = result.getProjectImage().isEmpty() ? null : result.getProjectImage().get(0).getUrl();
                    projectLists.add(new ProjectRes.ProjectList(
                            result.getId(),
                            result.getProjectName(),
                            result.getUsages(),
                            imageUrl
                    ));
                }
        );


        return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectLists);
    }

}

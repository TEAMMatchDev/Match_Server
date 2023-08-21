package com.example.adminapi.project.covertor;

import com.example.adminapi.project.dto.ProjectReq;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.project.entity.ImageRepresentStatus;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.entity.ProjectImage;

import static com.example.matchdomain.project.entity.ProjectStatus.BEFORE_START;

@Convertor
public class ProjectConvertor {
    public Project postProject(ProjectReq.Project projects) {
        return Project.builder()
                .projectName(projects.getProjectName())
                .projectExplanation(projects.getDetail())
                .usages(projects.getUsages())
                .projectStatus(BEFORE_START)
                .viewCnt(0)
                .startedAt(projects.getStartDate())
                .finishedAt(projects.getEndDate())
                .build();
    }

    public ProjectImage postProjectImage(Long id, String imgUrl, ImageRepresentStatus imageRepresentStatus, int sequence) {
        return ProjectImage.builder()
                .projectId(id)
                .url(imgUrl)
                .imageRepresentStatus(imageRepresentStatus)
                .sequence(sequence)
                .build();
    }
}

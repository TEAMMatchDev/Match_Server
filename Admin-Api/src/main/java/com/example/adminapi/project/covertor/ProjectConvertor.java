package com.example.adminapi.project.covertor;

import com.example.adminapi.project.dto.ProjectReq;
import com.example.adminapi.project.dto.ProjectRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.project.entity.ImageRepresentStatus;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.entity.ProjectImage;
import com.example.matchdomain.project.repository.ProjectRepository;

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

    public ProjectRes.ProjectList ProjectList(ProjectRepository.ProjectAdminList result) {
        return ProjectRes.ProjectList.builder()
                .projectId(result.getProjectId())
                .projectName(result.getProjectName())
                .totalAmount(result.getTotalAmount())
                .usages(result.getUsages())
                .totalDonationCnt(result.getTotalDonationCnt())
                .build();
    }
}

package com.example.matchapi.project.convertor;

import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.project.entity.ProjectImage;

import java.util.ArrayList;
import java.util.List;

@Convertor
public class ProjectConvertor {
    public ProjectRes.ProjectDetail projectImgList(List<ProjectImage> projectImage) {
        List<ProjectRes.ProjectImgList> projectImgList = new ArrayList<>();
        projectImage.forEach(
                result -> projectImgList.add(
                        new ProjectRes.ProjectImgList(
                                result.getId(),
                                result.getUrl(),
                                result.getSequence()
                        )
                )
        );
        return ProjectRes.ProjectDetail.builder()
                .projectId(projectImage.get(0).getProjectId())
                .title(projectImage.get(0).getProject().getProjectName())
                .usages(projectImage.get(0).getProject().getUsages())
                .projectImgList(projectImgList)
                .build();
    }
}

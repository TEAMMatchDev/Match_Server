package com.example.matchdomain.project.dto;

import com.example.matchdomain.project.entity.enums.ProjectKind;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {

    private Long id;
    private String usages;
    private ProjectKind projectKind;
    private Integer viewCnt;
    private String projectName;
    private String imgUrl;
    private boolean like;

    public boolean getLike() {
        return like;
    }
}

package com.example.matchdomain.project.dto;

import com.example.matchdomain.donation.entity.RegularPayment;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.entity.ProjectKind;
import com.example.matchdomain.user.entity.User;
import lombok.*;

import java.util.List;

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

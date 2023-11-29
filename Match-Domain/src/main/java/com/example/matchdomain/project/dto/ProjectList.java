package com.example.matchdomain.project.dto;

import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.project.entity.enums.ProjectKind;
import com.example.matchdomain.project.entity.enums.ProjectStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProjectList {
    private Long id;
    private String usages;
    private ProjectKind projectKind;
    private String projectName;
    private String searchKeyword;
    private String projectExplanation;
    private Status status;
    private LocalDateTime finishedAt;
    private ProjectStatus projectStatus;
    private Integer viewCnt;
    private String imgUrl;
    private Boolean like; // 추가
    private List<String> imgUrlList;
    private Long totalDonationCnt;

    public ProjectList() {
    }

    public ProjectList(
            Long id, String usages, ProjectKind projectKind, String projectName,
            String searchKeyword, String projectExplanation, Status status,
            LocalDateTime finishedAt, ProjectStatus projectStatus, Integer viewCnt,
            String imgUrl, Boolean like,  Long totalDonationCnt) {
        this.id = id;
        this.usages = usages;
        this.projectKind = projectKind;
        this.projectName = projectName;
        this.searchKeyword = searchKeyword;
        this.projectExplanation = projectExplanation;
        this.status = status;
        this.finishedAt = finishedAt;
        this.projectStatus = projectStatus;
        this.viewCnt = viewCnt;
        this.imgUrl = imgUrl;
        this.like = like;
        this.totalDonationCnt = totalDonationCnt;
    }
    public boolean getLike(){
        return like;
    }
}

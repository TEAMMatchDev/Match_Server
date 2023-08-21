package com.example.matchapi.project.helper;

import com.example.matchcommon.annotation.Helper;
import com.example.matchdomain.project.entity.Project;

import java.time.LocalDateTime;

import static com.example.matchdomain.project.entity.ProjectStatus.PROCEEDING;

@Helper
public class ProjectHelper {
    public boolean checkDonationAble(Project project){
        boolean donationAble = true;

        if (project.getProjectStatus()!=PROCEEDING) donationAble = false;

        if (project.getFinishedAt().compareTo(LocalDateTime.now()) <= 0) donationAble = false;

        return donationAble;
    }
}

package com.example.matchapi.project.helper;

import com.example.matchapi.project.convertor.ProjectConvertor;
import com.example.matchcommon.annotation.Helper;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.entity.ProjectImage;
import com.example.matchdomain.project.repository.ProjectImageRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.matchdomain.project.entity.ImageRepresentStatus.NORMAL;
import static com.example.matchdomain.project.entity.ImageRepresentStatus.REPRESENT;
import static com.example.matchdomain.project.entity.ProjectStatus.PROCEEDING;

@Helper
@RequiredArgsConstructor
public class ProjectHelper {
    public boolean checkDonationAble(Project project){
        boolean donationAble = true;

        if (project.getProjectStatus()!=PROCEEDING) donationAble = false;

        if (project.getFinishedAt().compareTo(LocalDateTime.now()) <= 0) donationAble = false;

        return donationAble;
    }

}

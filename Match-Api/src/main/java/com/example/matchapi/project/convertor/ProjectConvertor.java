package com.example.matchapi.project.convertor;

import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchapi.project.helper.ProjectHelper;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.entity.ProjectImage;
import com.example.matchdomain.project.entity.ProjectUserAttention;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.matchdomain.donation.entity.DonationStatus.*;
import static com.example.matchdomain.project.entity.ProjectStatus.PROCEEDING;

@Convertor
@RequiredArgsConstructor
public class ProjectConvertor {
    private final ProjectHelper projectHelper;
    public ProjectRes.ProjectDetail projectImgList(List<ProjectImage> projectImage) {
        List<ProjectRes.ProjectImgList> projectImgList = new ArrayList<>();

        boolean donationAble = projectHelper.checkDonationAble(projectImage.get(0).getProject());


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
                .donationAble(donationAble)
                .build();
    }


    public UserRes.MyPage getMyPage(List<DonationUser> donationUserList, List<ProjectUserAttention> project) {
        int beforeCnt=0;
        int underCnt=0;
        int successCnt=0;

        for (DonationUser donationUser : donationUserList) {
            if(donationUser.getDonationStatus()==EXECUTION_BEFORE){
                beforeCnt+=1;
            }else if(donationUser.getDonationStatus()==EXECUTION_UNDER){
                underCnt+=1;
            }else if(donationUser.getDonationStatus()==EXECUTION_SUCCESS){
                successCnt+=1;
            }
        }
        List<ProjectRes.ProjectList> projectList = new ArrayList<>();

        project.forEach(
                result -> {
                    String imageUrl = result.getProject().getProjectImage().isEmpty() ? null : result.getProject().getProjectImage().get(0).getUrl();
                    projectList.add(
                            new ProjectRes.ProjectList(
                                    result.getProject().getId(),
                                    imageUrl,
                                    result.getProject().getProjectName() ,
                                    result.getProject().getUsages())
                    );
                }

        );

        return UserRes.MyPage.builder()
                .beforeCnt(beforeCnt)
                .underCnt(underCnt)
                .successCnt(successCnt)
                .projectList(projectList)
                .build();



    }


}

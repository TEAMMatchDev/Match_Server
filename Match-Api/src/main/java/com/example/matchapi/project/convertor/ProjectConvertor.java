package com.example.matchapi.project.convertor;

import com.example.matchapi.project.dto.ProjectReq;
import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchapi.project.helper.ProjectHelper;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchcommon.annotation.Convertor;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.project.entity.*;
import com.example.matchdomain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.example.matchdomain.donation.entity.DonationStatus.*;
import static com.example.matchdomain.project.entity.ProjectStatus.BEFORE_START;

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
                .kind(projectImage.get(0).getProject().getProjectKind().getValue())
                .regularStatus(projectImage.get(0).getProject().getRegularStatus().getValue())
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
                                    result.getProject().getUsages(),
                                    result.getProject().getProjectKind().getValue(),
                                    false
                    ));
                }

        );

        return UserRes.MyPage.builder()
                .beforeCnt(beforeCnt)
                .underCnt(underCnt)
                .successCnt(successCnt)
                .projectList(projectList)
                .build();



    }


    public ProjectRes.CommentList projectComment(Long userId, ProjectComment result) {
        return ProjectRes.CommentList.builder()
                .commentId(result.getId())
                .comment(result.getComment())
                .commentDate(result.getCreatedAt().getDayOfYear()+"."+result.getCreatedAt().getDayOfMonth()+"."+result.getCreatedAt().getDayOfYear()+". " + result.getCreatedAt().getHour()+":"+result.getCreatedAt().getMinute())
                .nickname(result.getUser().getNickname())
                .userId(result.getUserId())
                .isMy(result.getUserId().equals(userId))
                .build();
    }

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

    public ProjectRes.ProjectAdminList ProjectList(ProjectRepository.ProjectAdminList result) {
        return ProjectRes.ProjectAdminList.builder()
                .projectId(result.getProjectId())
                .projectName(result.getProjectName())
                .totalAmount(result.getTotalAmount())
                .usages(result.getUsages())
                .totalDonationCnt(result.getTotalDonationCnt())
                .projectStatus(result.getProjectStatus().getName())
                .regularStatus(result.getRegularStatus().getName())
                .status(result.getStatus().getName())
                .build();
    }

    public ProjectRes.ProjectAdminDetail ProjectAdminDetail(ProjectRepository.ProjectAdminDetail result, List<ProjectImage> projectImages) {
        List<ProjectRes.ProjectImgList> projectImgLists = new ArrayList<>();
        projectImages.forEach(
                results -> projectImgLists.add(
                        new ProjectRes.ProjectImgList(
                                results.getId(),
                                results.getUrl(),
                                results.getSequence()
                        )
                )
        );


        return ProjectRes.ProjectAdminDetail
                .builder()
                .projectId(result.getProjectId())
                .projectName(result.getProjectName())
                .detail(result.getDetail())
                .usages(result.getUsages())
                .startDate(result.getStartDate().toString())
                .endDate(result.getEndDate().toString())
                .projectStatus(result.getProjectStatus().getValue())
                .regularStatus(result.getRegularStatus().getValue())
                .regularDonationCnt(result.getRegularTotalCnt())
                .status(result.getStatus().getValue())
                .totalAmount(result.getTotalAmount())
                .totalDonationCnt(result.getTotalDonationCnt())
                .projectImgLists(projectImgLists)
                .build();
    }

    public ProjectRes.DonationList DonationUserInfo(DonationUser result) {
        return ProjectRes.DonationList
                .builder()
                .donationId(result.getId())
                .userId(result.getUserId())
                .name(result.getUser().getName())
                .email(result.getUser().getEmail())
                .phoneNumber(result.getUser().getPhoneNumber())
                .amount(result.getPrice())
                .inherenceName(result.getInherenceName())
                .inherenceNumber(result.getInherenceNumber())
                .payMethod(result.getPayMethod().getValue())
                .donationStatus(result.getDonationStatus().getValue())
                .regularStatus(result.getRegularStatus().getValue())
                .donationDate(result.getCreatedAt().toString())
                .build();
    }
}

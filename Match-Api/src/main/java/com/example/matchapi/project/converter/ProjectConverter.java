package com.example.matchapi.project.converter;

import com.example.matchapi.common.util.TimeHelper;
import com.example.matchapi.donation.helper.DonationHelper;
import com.example.matchapi.project.dto.ProjectReq;
import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchapi.project.helper.ProjectHelper;
import com.example.matchapi.user.dto.UserRes;
import com.example.matchcommon.annotation.Converter;
import com.example.matchdomain.donation.entity.*;
import com.example.matchdomain.donation.entity.enums.HistoryStatus;
import com.example.matchdomain.donation.entity.enums.RegularPayStatus;
import com.example.matchdomain.donation.repository.RegularPaymentRepository;
import com.example.matchdomain.project.dto.ProjectDto;
import com.example.matchdomain.project.dto.ProjectList;
import com.example.matchdomain.project.entity.*;
import com.example.matchdomain.project.entity.enums.ImageRepresentStatus;
import com.example.matchdomain.project.entity.enums.ReportReason;
import com.example.matchdomain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.matchdomain.project.entity.enums.ProjectStatus.BEFORE_START;

@Converter
@RequiredArgsConstructor
public class ProjectConverter {
    private final ProjectHelper projectHelper;
    private final RegularPaymentRepository regularPaymentRepository;
    private static final String FIRST_TIME = "T00:00:00";
    private static final String LAST_TIME = "T23:59:59";
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


    public UserRes.MyPage getMyPage(List<RegularPayment> regularPayments, Long likeCnt, String name) {
        int underCnt=0;
        int successCnt=0;

        for (RegularPayment regularPayment : regularPayments) {
            if(regularPayment.getRegularPayStatus().equals(RegularPayStatus.PROCEEDING)){
                underCnt+=1;
            }else{
                successCnt+=1;
            }
        }

        return UserRes.MyPage.builder()
                .name(name)
                .likeCnt(Math.toIntExact(likeCnt))
                .underCnt(underCnt)
                .successCnt(successCnt)
                .build();
    }

    public Project postProject(ProjectReq.Project projects) {
        return Project.builder()
                .projectName(projects.getProjectName())
                .projectExplanation(projects.getDetail())
                .usages(projects.getUsages())
                .projectStatus(BEFORE_START)
                .viewCnt(0)
                .startedAt(LocalDateTime.parse(projects.getStartDate()+FIRST_TIME))
                .finishedAt(LocalDateTime.parse(projects.getEndDate()+LAST_TIME))
                .regularStatus(projects.getRegularStatus())
                .projectKind(projects.getProjectKind())
                .searchKeyword(projects.getSearchKeyword())
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

    public ProjectRes.ProjectAdminList convertToProjectList(ProjectRepository.ProjectAdminList result) {
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

    public ProjectRes.ProjectAdminDetail convertToProjectAdminDetail(ProjectRepository.ProjectAdminDetail result, List<ProjectImage> projectImages) {
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
                .searchKeyword(result.getSearchKeyword())
                .totalDonationCnt(result.getTotalDonationCnt())
                .projectImgLists(projectImgLists)
                .build();
    }

    public List<ProjectRes.DonationList> convertToDonationUserInfo(List<DonationUser> donationUsers){
        List<ProjectRes.DonationList> donationLists = new ArrayList<>();
        donationUsers.forEach(
                result -> donationLists.add(
                        convertToDonationUserInfoDetail(result)
                )
        );

        return donationLists;
    }

    public ProjectRes.DonationList convertToDonationUserInfoDetail(DonationUser result) {
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
                .donationStatusValue(result.getDonationStatus().getName())
                .regularStatus(result.getRegularStatus().getValue())
                .donationDate(result.getCreatedAt().toString())
                .build();
    }

    public ProjectRes.ProjectLists convertToProjectListsDetail(ProjectRepository.ProjectList result) {
        List<String> imgUrlList = new ArrayList<>();
        if(result.getImgUrlList()!=null){
            imgUrlList = Stream.of(result.getImgUrlList().split(",")).collect(Collectors.toList());
        }
        if(imgUrlList.size() >3){
            imgUrlList = imgUrlList.subList(0,3);
        }
        return ProjectRes.ProjectLists
                .builder()
                .projectId(result.getId())
                .imgUrl(result.getImgUrl())
                .title(result.getProjectName())
                .usages(result.getUsages())
                .kind(result.getProjectKind())
                .like(result.getLike())
                .userProfileImages(imgUrlList)
                .totalDonationCnt(result.getTotalDonationCnt())
                .build();
    }

    public List<ProjectRes.ProjectLists> convertToProjectLists(List<ProjectRepository.ProjectList> projects){
        List<ProjectRes.ProjectLists> projectLists = new ArrayList<>();
        projects.forEach(
                result -> {
                    projectLists.add(convertToProjectListsDetail(result));
                }
        );

        return projectLists;
    }

    public ProjectRes.ProjectAppDetail convertToProjectAppDetail(ProjectRepository.ProjectDetail projects, List<ProjectImage> projectImages) {
        List<ProjectRes.ProjectImgList> projectImgLists = new ArrayList<>();
        String thumbNail = "";
        for(ProjectImage projectImage : projectImages){
            if(projectImage.getImageRepresentStatus() == ImageRepresentStatus.NORMAL){
                projectImgLists.add(convertToProjectImages(projectImage));
            }
            else {
                thumbNail = projectImage.getUrl();
            }
        }
        List<String> imgUrlList = new ArrayList<>();
        if(projects.getImgUrlList()!=null){
            imgUrlList = Stream.of(projects.getImgUrlList().split(",")).collect(Collectors.toList());
        }
        if(imgUrlList.size() >3){
            imgUrlList = imgUrlList.subList(0,3);
        }

        return ProjectRes.ProjectAppDetail
                .builder()
                .projectId(projects.getId())
                .thumbNail(thumbNail)
                .projectImgList(projectImgLists)
                .title(projects.getProjectName())
                .usages(projects.getUsages())
                .kind(projects.getProjectKind())
                .regularStatus(projects.getRegularStatus())
                .like(projects.getLike())
                .userProfileImages(imgUrlList)
                .totalDonationCnt(projects.getTotalDonationCnt())
                .build();
    }

    private ProjectRes.ProjectImgList convertToProjectImages(ProjectImage projectImage) {
        return ProjectRes.ProjectImgList
                .builder()
                .imgId(projectImage.getId())
                .sequence(projectImage.getSequence())
                .imgUrl(projectImage.getUrl())
                .build();
    }

    public ProjectRes.ProjectLists convertToProjectToDto(ProjectDto result) {
        List<String> imgUrlList = new ArrayList<>();
        List<RegularPayment> regularPayments = regularPaymentRepository.findByProjectIdAndRegularPayStatus(result.getId(), RegularPayStatus.PROCEEDING);

        for(RegularPayment regularPayment : regularPayments){
            imgUrlList.add(regularPayment.getUser().getProfileImgUrl());
        }

        return ProjectRes.ProjectLists
                .builder()
                .projectId(result.getId())
                .imgUrl(result.getImgUrl())
                .title(result.getProjectName())
                .usages(result.getUsages())
                .kind(result.getProjectKind().getName())
                .like(result.getLike())
                .userProfileImages(imgUrlList)
                .totalDonationCnt(imgUrlList.size())
                .build();
    }

    public ProjectComment convertToComment(Long id, Long projectId, String comment) {
        return ProjectComment
                .builder()
                .userId(id)
                .comment(comment)
                .projectId(projectId)
                .build();
    }

    public DonationHistory convertToDonationHistory(Long projectId, HistoryStatus historyStatus) {
        return DonationHistory
                .builder()
                .projectId(projectId)
                .historyStatus(historyStatus)
                .build();
    }

    public CommentReport convertToReportComment(Long commentId, ReportReason reportReason) {
        return CommentReport
                .builder()
                .commentId(commentId)
                .reportReason(reportReason)
                .build();
    }

    public List<ProjectRes.ProjectList> convertToProjectListWeb(List<ProjectRepository.ProjectList> projects) {
        List<ProjectRes.ProjectList> projectLists = new ArrayList<>();

        projects.forEach(
                result -> {
                    projectLists.add(new ProjectRes.ProjectList(
                            result.getId(),
                            result.getImgUrl(),
                            result.getProjectName(),
                            result.getUsages(),
                            result.getProjectKind(),
                            result.getLike()
                    ));
                }
        );

        return projectLists;
    }

    public List<ProjectRes.ProjectList> convertToProjectListWebForNotLogin(List<Project> projects) {
        List<ProjectRes.ProjectList> projectLists = new ArrayList<>();

        projects.forEach(
                result -> {
                    String imageUrl = result.getProjectImage().isEmpty() ? null : result.getProjectImage().get(result.getProjectImage().size()-1).getUrl();
                    projectLists.add(new ProjectRes.ProjectList(
                            result.getId(),
                            imageUrl,
                            result.getProjectName(),
                            result.getUsages(),
                            result.getProjectKind().getValue(),
                            false
                    ));
                }
        );
        return projectLists;
    }

    public ProjectRes.CommentList projectComment(Long userId, ProjectComment result) {
        return ProjectRes.CommentList.builder()
                .commentId(result.getId())
                .comment(result.getComment())
                .commentDate(result.getCreatedAt())
                .nickname(result.getUser().getNickname())
                .profileImgUrl(result.getUser().getProfileImgUrl())
                .userId(result.getUserId())
                .isMy(result.getUserId().equals(userId))
                .build();
    }
}

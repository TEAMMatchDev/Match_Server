package com.example.matchapi.project.service;

import com.example.matchapi.common.util.MessageHelper;
import com.example.matchapi.donation.dto.DonationRes;
import com.example.matchapi.project.converter.ProjectConverter;
import com.example.matchapi.project.dto.ProjectReq;
import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchcommon.constants.enums.FILTER;
import com.example.matchapi.user.helper.AuthHelper;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.adaptor.DonationAdaptor;
import com.example.matchdomain.donation.adaptor.DonationHistoryAdaptor;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.entity.enums.HistoryStatus;
import com.example.matchdomain.donation.entity.enums.RegularStatus;
import com.example.matchdomain.project.adaptor.ProjectAdaptor;
import com.example.matchdomain.project.adaptor.ProjectImgAdaptor;
import com.example.matchdomain.project.dto.ProjectDto;
import com.example.matchdomain.project.entity.*;
import com.example.matchdomain.project.entity.enums.ProjectKind;
import com.example.matchdomain.project.entity.enums.ProjectStatus;
import com.example.matchdomain.project.entity.enums.ReportReason;
import com.example.matchdomain.project.entity.pk.ProjectUserAttentionPk;
import com.example.matchdomain.project.repository.*;
import com.example.matchdomain.user.entity.User;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import com.example.matchcommon.constants.enums.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.matchcommon.constants.MatchAlertStatic.PROJECT_UPLOAD_BODY;
import static com.example.matchdomain.common.model.Status.ACTIVE;
import static com.example.matchdomain.common.model.Status.INACTIVE;
import static com.example.matchdomain.project.entity.enums.ImageRepresentStatus.NORMAL;
import static com.example.matchdomain.project.entity.enums.ImageRepresentStatus.REPRESENT;
import static com.example.matchdomain.project.entity.enums.ProjectStatus.PROCEEDING;
import static com.example.matchdomain.project.exception.CommentDeleteErrorCode.COMMENT_DELETE_ERROR_CODE;
import static com.example.matchdomain.project.exception.CommentGetErrorCode.COMMENT_NOT_EXIST;
import static com.example.matchdomain.project.exception.PatchProjectImageErrorCode.PROJECT_NOT_CORRECT_IMAGE;
import static com.example.matchdomain.project.exception.ProjectGetErrorCode.PROJECT_NOT_EXIST;


@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectAdaptor projectAdaptor;
    private final ProjectRepository projectRepository;
    private final ProjectConverter projectConverter;
    private final ProjectImageRepository projectImageRepository;
    private final AuthHelper authHelper;
    private final ProjectCommentRepository projectCommentRepository;
    private final S3UploadService s3UploadService;
    private final ProjectUserAttentionRepository projectUserAttentionRepository;
    private final DonationHistoryAdaptor donationHistoryAdaptor;
    private final CommentReportRepository commentReportRepository;
    private final ProjectImgAdaptor projectImgAdaptor;
    private final DonationAdaptor donationAdaptor;
    private final MessageHelper messageHelper;

    public PageResponse<List<ProjectRes.ProjectList>> getProjectList(User user, int page, int size) {
        Long userId = 0L;

        if(authHelper.checkGuest(user)) userId = user.getId();

        if(!userId.equals(0L)){
            Page<ProjectRepository.ProjectList> projects = projectAdaptor.findLoginUserProjectList(userId, page, size);

            return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectConverter.convertToProjectListWeb(projects.getContent()));
        } else{
            Page<Project> projects = projectAdaptor.findNotLoginUserProjectList(page, size);

            return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectConverter.convertToProjectListWebForNotLogin(projects.getContent()));
        }

    }

    public ProjectRes.ProjectDetail getProjectDetail(User user, Long projectId) {
        return projectConverter.projectImgList(projectImgAdaptor.getProjectDetail(projectId));
    }

    public PageResponse<List<ProjectRes.ProjectList>> searchProjectList(User user, String content, int page, int size) {
        Long userId = 0L;

        if(authHelper.checkGuest(user)) userId = user.getId();

        if(!userId.equals(0L)){
            Page<ProjectRepository.ProjectList> projects = projectAdaptor.findLoginUserSearchProjectList(userId, page, size, content);

            return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectConverter.convertToProjectListWeb(projects.getContent()));
        } else{
            Page<Project> projects = projectAdaptor.findNotLoginUserSearchProjectList(content, page, size);

            return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectConverter.convertToProjectListWebForNotLogin(projects.getContent()));
        }
    }


    @Transactional
    public void postProject(ProjectReq.Project projects, MultipartFile presentFile, List<MultipartFile> multipartFiles) {
        Project project = projectRepository.save(projectConverter.postProject(projects));

        String url = s3UploadService.uploadProjectPresentFile(project.getId() ,presentFile);

        List<String> imgUrlList = s3UploadService.listUploadProjectFiles(project.getId(), multipartFiles);

        saveImgList(project.getId(), url, imgUrlList);

        donationHistoryAdaptor.saveDonationHistory(projectConverter.convertToDonationHistory(project.getId(), HistoryStatus.TURN_ON));

        messageHelper.helpFcmMessage(PROJECT_UPLOAD_BODY, Topic.PROJECT_UPLOAD, project.getId());

    }

    public PageResponse<List<ProjectRes.ProjectAdminList>> getProjectList(int page, int size) {
        Pageable pageable  = PageRequest.of(page,size);

        Page<ProjectRepository.ProjectAdminList> projectAdminLists = projectRepository.getProjectAdminList(pageable);

        List<ProjectRes.ProjectAdminList> projectLists = new ArrayList<>();

        projectAdminLists.getContent().forEach(
                result -> projectLists.add(
                        projectConverter.convertToProjectList(result)
                )
        );

        return new PageResponse<>(projectAdminLists.isLast(), projectAdminLists.getTotalElements(), projectLists);
    }

    @Transactional
    public void patchProjectStatus(ProjectStatus projectStatus, Long projectId) {
        Project project = projectAdaptor.findById(projectId);

        project.setProjectStatus(projectStatus);

        if(projectStatus.equals(ProjectStatus.DEADLINE)) donationHistoryAdaptor.saveDonationHistory(projectConverter.convertToDonationHistory(projectId, HistoryStatus.FINISH));

        projectRepository.save(project);
    }

    public void deleteProject(Long projectId) {
        Project project = projectAdaptor.findById(projectId);

        project.setStatus(Status.INACTIVE);

        projectRepository.save(project);
    }

    @Transactional
    public void patchProject(Long projectId, ProjectReq.ModifyProject modifyProject) {
        Project project = projectAdaptor.findById(projectId);

        project.modifyProject(modifyProject.getProjectName(), modifyProject.getUsages(), modifyProject.getDetail(), modifyProject.getRegularStatus(), modifyProject.getStartDate(), modifyProject.getEndDate(), modifyProject.getProjectKind(), modifyProject.getSearchKeyword());

        projectRepository.save(project);
    }

    @Transactional
    public void saveImgList(Long id, String url, List<String> imgUrlList) {
        imgUrlList.add(url);
        List<ProjectImage> projectImages = new ArrayList<>();

        for (int i=1 ; i <= imgUrlList.size(); i++) {
            if(i==imgUrlList.size()){
                projectImages.add(projectConverter.postProjectImage(id,imgUrlList.get(i-1),REPRESENT,i));
            }else {
                projectImages.add(projectConverter.postProjectImage(id, imgUrlList.get(i-1),NORMAL, i));
            }
        }

        projectImageRepository.saveAll(projectImages);
    }

    @Transactional
    public ProjectRes.ProjectAdminDetail getProjectAdminDetail(Long projectId) {
        ProjectRepository.ProjectAdminDetail projectAdminDetail = projectAdaptor.projectAdminDetail(projectId);

        if(projectAdminDetail == null) throw new BadRequestException(PROJECT_NOT_EXIST);

        List<ProjectImage> projectImages = projectImgAdaptor.findProjectImages(projectId);

        return projectConverter.convertToProjectAdminDetail(projectAdminDetail,projectImages);
    }

    public PageResponse<List<ProjectRes.DonationList>> getDonationList(Long projectId, int page, int size) {
        Page<DonationUser> donationUsers = donationAdaptor.findDonationUsers(projectId, page, size);

        return new PageResponse<>(donationUsers.isLast(), donationUsers.getTotalElements(), projectConverter.convertToDonationUserInfo(donationUsers.getContent()));
    }

    @Transactional
    public ProjectRes.PatchProjectImg modifyProjectImg(Long projectId, Long projectImgId, MultipartFile multipartFile) {
        ProjectImage projectImage = projectImgAdaptor.findById(projectImgId);

        if(!projectImage.getProjectId().equals(projectId)) throw new BadRequestException(PROJECT_NOT_CORRECT_IMAGE);

        String imgUrl = s3UploadService.uploadProjectPresentFile(projectId, multipartFile);

        s3UploadService.deleteFile(projectImage.getUrl());

        projectImage.setUrl(imgUrl);

        projectImageRepository.save(projectImage);

        return new ProjectRes.PatchProjectImg(projectImgId, projectImage.getUrl());
    }

    public void patchProjectActive(Long projectId) {
        Project project = projectAdaptor.findById(projectId);

        project.setStatus(ACTIVE);

        projectRepository.save(project);
    }

    public PageResponse<List<ProjectRes.ProjectLists>> getProjectLists(User user, int page, int size, ProjectKind projectKind, String content, FILTER filter) {
        Page<ProjectRepository.ProjectList> projects = projectAdaptor.findProject(user, page, size, projectKind, content, filter);

        return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectConverter.convertToProjectLists(projects.getContent()));
    }

    @Transactional
    public ProjectRes.ProjectLike patchProjectLike(User user, Long projectId) {
        boolean checkProjectLike = projectUserAttentionRepository.existsById_userIdAndId_projectId(user.getId(), projectId);
        if(checkProjectLike){
            projectUserAttentionRepository.deleteById_userIdAndId_projectId(user.getId(), projectId);
        }else{
            projectUserAttentionRepository.save(ProjectUserAttention.builder().id(new ProjectUserAttentionPk(user.getId(), projectId)).build());
        }

        return new ProjectRes.ProjectLike(!checkProjectLike);
    }

    public PageResponse<List<ProjectRes.ProjectLists>> getTodayProjectLists(User user, int page, int size) {
        Page<ProjectRepository.ProjectList> projects = projectAdaptor.getTodayProjectLists(user.getId(), page, size);

        return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectConverter.convertToProjectLists(projects.getContent()));
    }

    public ProjectRes.ProjectAppDetail getProjectAppDetail(User user, Long projectId) {
        ProjectRepository.ProjectDetail projects = projectRepository.getProjectAppDetail(user.getId(), projectId);
        List<ProjectImage> projectImages = projectImageRepository.findByProjectIdOrderBySequenceAsc(projectId);

        return projectConverter.convertToProjectAppDetail(projects, projectImages);
    }

    public PageResponse<List<ProjectRes.ProjectLists>> projectList(User user, int page, int size, ProjectKind projectKind, String content) {
        Pageable pageable = PageRequest.of(page, size);
        List<ProjectRes.ProjectLists> project = new ArrayList<>();

        Page<ProjectDto> projects = projectRepository.findProject(user, PROCEEDING, LocalDateTime.now(),
                REPRESENT, ACTIVE, projectKind, content,  pageable);


        projects.getContent().forEach(
                result -> {
                    project.add(projectConverter.convertToProjectToDto(result));
                }
        );

        return new PageResponse<>(projects.isLast(), projects.getTotalElements(), project);
    }


    public ProjectRes.CommentList postComment(User user, Long projectId, ProjectReq.Comment comment) {
        ProjectComment projectComment = projectCommentRepository.save(projectConverter.convertToComment(user.getId(), projectId, comment.getComment()));

        return projectConverter.projectComment(user, projectComment);
    }

    public void reportComment(Long commentId, ReportReason reportReason) {
        ProjectComment projectComment = projectCommentRepository.findByIdAndStatus(commentId, ACTIVE).orElseThrow(()-> new NotFoundException(COMMENT_NOT_EXIST));

        commentReportRepository.save(projectConverter.convertToReportComment(commentId, reportReason));
    }

    public void deleteComment(User user, Long commentId) {
        ProjectComment projectComment = projectCommentRepository.findByIdAndStatus(commentId, ACTIVE).orElseThrow(()-> new NotFoundException(COMMENT_NOT_EXIST));
        if(!projectComment.getUserId().equals(user.getId())) throw new BadRequestException(COMMENT_DELETE_ERROR_CODE);
        projectComment.setStatus(INACTIVE);
        projectCommentRepository.save(projectComment);
    }

    public Project checkProjectExists(Long projectId, RegularStatus regularStatus) {
        return projectAdaptor.checkRegularProjects(projectId, regularStatus);
    }

    public PageResponse<List<ProjectRes.CommentList>> getProjectComment(User user, Long projectId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);


        Page<ProjectComment> projectComments = projectCommentRepository.findByProjectIdAndStatusOrderByCreatedAtAsc(projectId, ACTIVE,pageable);

        List<ProjectRes.CommentList> commentLists = new ArrayList<>();
        projectComments.getContent().forEach(
                result-> {
                    commentLists.add(
                            projectConverter.projectComment(user, result)
                    );
                }
        );


        return new PageResponse<>(projectComments.isLast(), projectComments.getTotalElements(), commentLists);
    }

    public PageResponse<List<ProjectRes.ProjectLists>> getLikeProjects(User user, int page, int size) {
        Page<ProjectRepository.ProjectList> projects = projectAdaptor.findLikeProjects(user, page, size);
        return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectConverter.convertToProjectLists(projects.getContent()));
    }

    public Project findByProject(String projectId) {
        return projectAdaptor.findByProject(projectId);
    }

    public Project findByProjectId(Long projectId) {
        return projectAdaptor.findById(projectId);
    }

    public List<DonationRes.Tutorial> getTutorialDonation() {
        List<Project> projects = projectAdaptor.getRandom3Project();
        return projectConverter.convertToTutorialDonation(projects);
    }
}

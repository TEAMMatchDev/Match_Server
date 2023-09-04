package com.example.matchapi.project.service;

import com.example.matchapi.project.convertor.ProjectConvertor;
import com.example.matchapi.project.dto.ProjectReq;
import com.example.matchapi.project.dto.ProjectRes;
import com.example.matchapi.user.helper.AuthHelper;
import com.example.matchcommon.exception.NotFoundException;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.common.model.Status;
import com.example.matchdomain.donation.entity.DonationUser;
import com.example.matchdomain.donation.repository.DonationUserRepository;
import com.example.matchdomain.project.entity.*;
import com.example.matchdomain.project.repository.ProjectCommentRepository;
import com.example.matchdomain.project.repository.ProjectImageRepository;
import com.example.matchdomain.project.repository.ProjectRepository;
import com.example.matchdomain.user.entity.User;
import com.example.matchinfrastructure.config.s3.S3UploadService;
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

import static com.example.matchdomain.project.entity.ImageRepresentStatus.NORMAL;
import static com.example.matchdomain.project.entity.ImageRepresentStatus.REPRESENT;
import static com.example.matchdomain.project.exception.ProjectGetErrorCode.PROJECT_NOT_EXIST;


@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectConvertor projectConvertor;
    private final ProjectImageRepository projectImageRepository;
    private final AuthHelper authHelper;
    private final ProjectCommentRepository projectCommentRepository;
    private final S3UploadService s3UploadService;
    private final DonationUserRepository donationUserRepository;

    public PageResponse<List<ProjectRes.ProjectList>> getProjectList(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Long userId = 0L;
        if(authHelper.checkGuest(user)) userId = user.getId();


        List<ProjectRes.ProjectList> projectLists = new ArrayList<>();

        if(!userId.equals(0L)){
            Page<ProjectRepository.ProjectList> projects = projectRepository.findLoginUserProjectList(userId, ProjectStatus.PROCEEDING.getValue(), LocalDateTime.now(), ImageRepresentStatus.REPRESENT.getValue(), pageable, Status.ACTIVE.getValue());
            projects.getContent().forEach(
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
            return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectLists);
        }

        else{
            Page<Project> projects = projectRepository.findByStatusAndProjectStatusAndFinishedAtGreaterThanEqualAndProjectImage_ImageRepresentStatusOrderByViewCnt(Status.ACTIVE,ProjectStatus.PROCEEDING, LocalDateTime.now(), ImageRepresentStatus.REPRESENT, pageable);
            projects.getContent().forEach(
                    result -> {
                        String imageUrl = result.getProjectImage().isEmpty() ? null : result.getProjectImage().get(0).getUrl();
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
            return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectLists);
        }

    }

    public ProjectRes.ProjectDetail getProjectDetail(User user, Long projectId) {
        List<ProjectImage> projectImage = projectImageRepository.findByProjectIdAndImageRepresentStatusAndProject_StatusOrderBySequenceAsc(projectId, ImageRepresentStatus.NORMAL, Status.ACTIVE);
        return projectConvertor.projectImgList(projectImage);
    }

    public PageResponse<List<ProjectRes.ProjectList>> searchProjectList(User user, String content, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);


        List<ProjectRes.ProjectList> projectLists = new ArrayList<>();

        Long userId = 0L;
        if(authHelper.checkGuest(user)) userId = user.getId();

        if(!userId.equals(0L)){

            Page<ProjectRepository.ProjectList> projects = projectRepository.searchProjectLoginUser(userId,content,content,content,ProjectStatus.PROCEEDING.getValue(),LocalDateTime.now(), ImageRepresentStatus.REPRESENT.getValue(),pageable,Status.ACTIVE.getValue());

            projects.getContent().forEach(
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


            return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectLists);

        }
        else{
            Page<Project> projects = projectRepository.searchProject(content,content,content,ProjectStatus.PROCEEDING,LocalDateTime.now(),ImageRepresentStatus.REPRESENT,pageable, Status.ACTIVE);

            projects.getContent().forEach(
                    result -> {
                        String imageUrl = result.getProjectImage().isEmpty() ? null : result.getProjectImage().get(0).getUrl();
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


            return new PageResponse<>(projects.isLast(), projects.getTotalElements(), projectLists);
        }

    }

    public PageResponse<List<ProjectRes.CommentList>> getProjectComment(User user, Long projectId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Long userId;
        if(authHelper.checkGuest(user)) userId = user.getId();
        else {
            userId = 0L;
        }

        Page<ProjectComment> projectComments = projectCommentRepository.findByProjectIdAndStatusOrderByCreatedAtDesc(projectId,Status.ACTIVE,pageable);

        List<ProjectRes.CommentList> commentLists = new ArrayList<>();
        projectComments.getContent().forEach(
                result-> {
                    commentLists.add(
                            projectConvertor.projectComment(userId, result)
                    );
                }
        );


        return null;
    }


    @Transactional
    public void postProject(ProjectReq.Project projects, MultipartFile presentFile, List<MultipartFile> multipartFiles) {

        System.out.println(projects.getProjectName());
        Project project = projectRepository.save(projectConvertor.postProject(projects));

        String url = s3UploadService.uploadProjectPresentFile(project.getId() ,presentFile);

        List<String> imgUrlList = s3UploadService.listUploadProjectFiles(project.getId(), multipartFiles);

        saveImgList(project.getId(), url, imgUrlList);

        System.out.println(url);
    }

    public PageResponse<List<ProjectRes.ProjectAdminList>> getProjectList(int page, int size) {
        Pageable pageable  = PageRequest.of(page,size);

        Page<ProjectRepository.ProjectAdminList> projectAdminLists = projectRepository.getProjectAdminList(pageable, Status.ACTIVE.getValue());

        List<ProjectRes.ProjectAdminList> projectLists = new ArrayList<>();

        projectAdminLists.getContent().forEach(
                result -> projectLists.add(
                        projectConvertor.ProjectList(result)
                )
        );

        return new PageResponse<>(projectAdminLists.isLast(), projectAdminLists.getTotalElements(), projectLists);
    }

    @Transactional
    public void patchProjectStatus(ProjectStatus projectStatus, Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(()-> new NotFoundException(PROJECT_NOT_EXIST));

        project.setProjectStatus(projectStatus);

        projectRepository.save(project);
    }

    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(()-> new NotFoundException(PROJECT_NOT_EXIST));

        project.setStatus(Status.INACTIVE);

        projectRepository.save(project);
    }

    public void patchProject(Long projectId, ProjectReq.ModifyProject modifyProject) {
        Project project = projectRepository.findById(projectId).orElseThrow(()-> new NotFoundException(PROJECT_NOT_EXIST));

        project.modifyProject(modifyProject.getProjectName(), modifyProject.getUsages(), modifyProject.getDetail(), modifyProject.getRegularStatus(), modifyProject.getStartDate(), modifyProject.getEndDate(), modifyProject.getProjectKind());

        projectRepository.save(project);
    }

    public void saveImgList(Long id, String url, List<String> imgUrlList) {
        imgUrlList.add(url);
        List<ProjectImage> projectImages = new ArrayList<>();

        for (int i=1 ; i <= imgUrlList.size(); i++) {
            if(i==imgUrlList.size()){
                projectImages.add(projectConvertor.postProjectImage(id,imgUrlList.get(i-1),REPRESENT,i));
            }else {
                projectImages.add(projectConvertor.postProjectImage(id, imgUrlList.get(i-1),NORMAL, i));
            }
        }

        projectImageRepository.saveAll(projectImages);
    }

    public ProjectRes.ProjectAdminDetail getProjectAdminDetail(Long projectId) {
        ProjectRepository.ProjectAdminDetail projectAdminDetail = projectRepository.getProjectAdminDetail(projectId);
        if(projectAdminDetail == null) throw new NotFoundException(PROJECT_NOT_EXIST);
        List<ProjectImage> projectImages = projectImageRepository.findByProjectIdOrderBySequenceAsc(projectId);
        return projectConvertor.ProjectAdminDetail(projectAdminDetail,projectImages);
    }

    public PageResponse<List<ProjectRes.DonationList>> getDonationList(Long projectId, int page, int size) {
        Project project = projectRepository.findById(projectId).orElseThrow(()-> new NotFoundException(PROJECT_NOT_EXIST));

        Pageable pageable = PageRequest.of(page, size);

        List<ProjectRes.DonationList> donationLists = new ArrayList<>();

        Page<DonationUser> donationUsers = donationUserRepository.findByProjectId(projectId, pageable);

        donationUsers.getContent().forEach(
                result -> donationLists.add(
                        projectConvertor.DonationUserInfo(result)
                )
        );

        return new PageResponse(donationUsers.isLast(), donationUsers.getTotalElements(), donationLists);
    }
}

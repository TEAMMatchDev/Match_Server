package com.example.adminapi.project.service;

import com.example.adminapi.project.covertor.ProjectConvertor;
import com.example.adminapi.project.dto.ProjectReq;
import com.example.adminapi.project.dto.ProjectRes;
import com.example.adminapi.project.helper.ProjectHelper;
import com.example.matchcommon.reponse.PageResponse;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.repository.ProjectImageRepository;
import com.example.matchdomain.project.repository.ProjectRepository;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final S3UploadService s3UploadService;
    private final ProjectConvertor projectConvertor;
    private final ProjectRepository projectRepository;
    private final ProjectImageRepository projectImageRepository;
    private final ProjectHelper projectHelper;

    @Transactional
    public void postProject(ProjectReq.Project projects, MultipartFile presentFile, List<MultipartFile> multipartFiles) {

        System.out.println(projects.getProjectName());
        Project project = projectRepository.save(projectConvertor.postProject(projects));

        String url = s3UploadService.uploadProjectPresentFile(project.getId() ,presentFile);

        List<String> imgUrlList = s3UploadService.listUploadProjectFiles(project.getId(), multipartFiles);

        projectHelper.saveImgList(project.getId(), url, imgUrlList);





        System.out.println(url);
    }

    public PageResponse<List<ProjectRes.ProjectList>> getProjectList(int page, int size) {
        Pageable pageable  = PageRequest.of(page,size);

        Page<ProjectRepository.ProjectAdminList> projectAdminLists = projectRepository.getProjectAdminList(pageable);

        List<ProjectRes.ProjectList> projectLists = new ArrayList<>();

        projectAdminLists.getContent().forEach(
                result -> projectLists.add(
                        projectConvertor.ProjectList(result)
                )
        );

        return new PageResponse<>(projectAdminLists.isLast(), projectAdminLists.getTotalElements(), projectLists);
    }
}

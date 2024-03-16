package com.example.matchapi.project.service;

import com.example.matchapi.project.converter.ProjectConverter;
import com.example.matchdomain.project.adaptor.ProjectImgAdaptor;
import com.example.matchdomain.project.entity.Project;
import com.example.matchdomain.project.entity.ProjectImage;
import com.example.matchinfrastructure.config.s3.S3UploadService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.example.matchdomain.project.entity.enums.ImageRepresentStatus.NORMAL;
import static com.example.matchdomain.project.entity.enums.ImageRepresentStatus.REPRESENT;

@Service
@RequiredArgsConstructor
public class ProjectImgService {
    private final ProjectImgAdaptor projectImgAdaptor;
    private final ProjectConverter projectConverter;
    private final S3UploadService s3UploadService;

    public void saveImgList(Long projectId, String url, List<String> imgUrlList) {
        imgUrlList.add(url);
        List<ProjectImage> projectImages = new ArrayList<>();

        for (int i=1 ; i <= imgUrlList.size(); i++) {
            if(i==imgUrlList.size()){
                projectImages.add(projectConverter.toProductImage(projectId,imgUrlList.get(i-1),REPRESENT,i));
            }else {
                projectImages.add(projectConverter.toProductImage(projectId, imgUrlList.get(i-1),NORMAL, i));
            }
        }

        projectImgAdaptor.saveAll(projectImages);
    }

    public List<ProjectImage> findByProjectId(Long projectId) {
        return projectImgAdaptor.findProjectImages(projectId);
    }

    public ProjectImage save(ProjectImage projectImage) {
        return projectImgAdaptor.save(projectImage);
    }

    public void updateImageLists(Project project, List<Long> deleteImageList, MultipartFile presentFile, List<MultipartFile> multipartFiles) {
        List<ProjectImage> projectsImage = project.getProjectImage();
        if(deleteImageList != null){
            for (ProjectImage projectImage : projectsImage) {
                if(deleteImageList.contains(projectImage.getId())){
                    s3UploadService.deleteFile(projectImage.getUrl());
                }
            }
            projectImgAdaptor.deleteImgList(deleteImageList);
        }

        if(presentFile != null){
            ProjectImage projectImage = projectImgAdaptor.getRepresentImage(project.getId(), REPRESENT);
            s3UploadService.deleteFile(projectImage.getUrl());
            projectImage.setUrl(s3UploadService.uploadProjectPresentFile(project.getId(), presentFile));
            projectImgAdaptor.save(projectImage);
        }
        if(multipartFiles != null){
            List<String> imgUrlList = s3UploadService.listUploadProjectFiles(project.getId(), multipartFiles);
            patchImageLists(project.getId(), imgUrlList, projectsImage.size()- (deleteImageList != null ?
                deleteImageList.size() : 0));
        }
    }

    public void patchImageLists(Long projectId, List<String> imgUrlList, int size) {
        List<ProjectImage> projectImages = new ArrayList<>();

        for (int i=1 ; i <= imgUrlList.size(); i++) {
            projectImages.add(projectConverter.toProductImage(projectId,imgUrlList.get(i-1),NORMAL,size+i));
        }

        projectImgAdaptor.saveAll(projectImages);
    }
}

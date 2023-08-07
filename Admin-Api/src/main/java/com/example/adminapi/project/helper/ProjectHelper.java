package com.example.adminapi.project.helper;

import com.example.adminapi.project.covertor.ProjectConvertor;
import com.example.matchcommon.annotation.Helper;
import com.example.matchdomain.project.entity.ProjectImage;
import com.example.matchdomain.project.repository.ProjectImageRepository;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.example.matchdomain.project.entity.ImageRepresentStatus.NORMAL;
import static com.example.matchdomain.project.entity.ImageRepresentStatus.REPRESENT;

@Helper
@RequiredArgsConstructor
public class ProjectHelper {
    private final ProjectImageRepository projectImageRepository;
    private final ProjectConvertor projectConvertor;

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
}

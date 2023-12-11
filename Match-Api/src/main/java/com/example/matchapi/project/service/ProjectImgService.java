package com.example.matchapi.project.service;

import com.example.matchapi.project.converter.ProjectConverter;
import com.example.matchdomain.project.adaptor.ProjectImgAdaptor;
import com.example.matchdomain.project.entity.ProjectImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.example.matchdomain.project.entity.enums.ImageRepresentStatus.NORMAL;
import static com.example.matchdomain.project.entity.enums.ImageRepresentStatus.REPRESENT;

@Service
@RequiredArgsConstructor
public class ProjectImgService {
    private final ProjectImgAdaptor projectImgAdaptor;
    private final ProjectConverter projectConverter;

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

        projectImgAdaptor.saveAll(projectImages);
    }

    public List<ProjectImage> findByProjectId(Long projectId) {
        return projectImgAdaptor.findProjectImages(projectId);
    }

    public ProjectImage save(ProjectImage projectImage) {
        return projectImgAdaptor.save(projectImage);
    }
}

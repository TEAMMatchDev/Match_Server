package com.example.matchdomain.project.adaptor;

import com.example.matchcommon.annotation.Adaptor;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchdomain.project.entity.ProjectImage;
import com.example.matchdomain.project.entity.enums.ImageRepresentStatus;
import com.example.matchdomain.project.repository.ProjectImageRepository;
import com.example.matchdomain.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.matchdomain.common.model.Status.ACTIVE;
import static com.example.matchdomain.project.exception.PatchProjectImageErrorCode.PROJECT_IMAGE_NOT_EXIST;

@Adaptor
@RequiredArgsConstructor
public class ProjectImgAdaptor {
    private final ProjectImageRepository projectImageRepository;

    public List<ProjectImage> getProjectDetail(Long projectId) {
        return projectImageRepository.findByProjectIdAndImageRepresentStatusAndProject_StatusOrderBySequenceAsc(projectId, ImageRepresentStatus.NORMAL, ACTIVE);
    }

    public List<ProjectImage> findProjectImages(Long projectId) {
        return projectImageRepository.findByProjectIdOrderBySequenceAsc(projectId);
    }

    public ProjectImage findById(Long projectImgId) {
        return  projectImageRepository.findById(projectImgId).orElseThrow(()-> new BadRequestException(PROJECT_IMAGE_NOT_EXIST));
    }

    public void saveAll(List<ProjectImage> projectImages) {
        projectImageRepository.saveAll(projectImages);
    }

    public ProjectImage save(ProjectImage projectImage) {
        return projectImageRepository.save(projectImage);
    }

	public void deleteImgList(List<Long> deleteImageList) {
        projectImageRepository.deleteAllByIdIn(deleteImageList);
	}

    public List<ProjectImage> findByIdIn(List<Long> deleteImageList) {
        return projectImageRepository.findAllById(deleteImageList);
    }

    public void deletePresentImg(Long id) {
        projectImageRepository.deleteByIdAndImageRepresentStatus(id, ImageRepresentStatus.REPRESENT);
    }
}

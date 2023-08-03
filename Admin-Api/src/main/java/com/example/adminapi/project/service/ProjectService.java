package com.example.adminapi.project.service;

import com.example.adminapi.project.dto.ProjectReq;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final S3UploadService s3UploadService;
    public void postProject(ProjectReq.Project project, MultipartFile presentFile, List<MultipartFile> multipartFiles) {
        String url = s3UploadService.uploadProjectPresentFile(1L ,presentFile);
        System.out.println(url);
    }
}

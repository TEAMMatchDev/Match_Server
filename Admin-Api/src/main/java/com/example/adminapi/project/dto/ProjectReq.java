package com.example.adminapi.project.dto;

import com.example.matchdomain.project.entity.ImageRepresentStatus;
import com.example.matchdomain.project.entity.RegularStatus;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ProjectReq {
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Project{
        private String projectName;

        private String usages;

        private RegularStatus regularStatus;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectImg{
        private ImageRepresentStatus imageRepresentStatus;

        private MultipartFile multipartFile;

        private int sequence;
    }
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProjectImgUploadResult{
        private ImageRepresentStatus imageRepresentStatus;

        private String imgUrl;

        private int sequence;
    }
}

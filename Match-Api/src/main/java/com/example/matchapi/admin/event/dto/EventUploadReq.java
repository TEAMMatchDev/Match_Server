package com.example.matchapi.admin.event.dto;

import com.example.matchdomain.common.model.ContentsType;
import com.example.matchdomain.event.enums.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventUploadReq {
    private String title;

    private String smallTitle;

    private String thumbnail;

    private EventType eventType;

    private List<ContentsList> contentsList;

    private LocalDate eventStartDate;

    private LocalDate eventEndDate;


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContentsList{
        @NotBlank(message = "Contents 타입을 입력해주새요")
        private ContentsType contentsType;

        @Schema(description = "이미지 url or Text", required = false)
        private String contents;
    }
}

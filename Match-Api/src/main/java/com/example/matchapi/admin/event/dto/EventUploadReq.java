package com.example.matchapi.admin.event.dto;

import com.example.matchapi.common.model.ContentsList;
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
@ToString
public class EventUploadReq {
    private String title;

    private String smallTitle;

    private EventType eventType;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<ContentsList> contentsList;

}

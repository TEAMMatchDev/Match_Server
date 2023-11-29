package com.example.matchapi.common.model;

import com.example.matchdomain.common.model.ContentsType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentsList{
    @NotBlank(message = "Contents 타입을 입력해주새요")
    private ContentsType contentsType;

    @Schema(description = "이미지 url or Text", required = false)
    private String contents;
}

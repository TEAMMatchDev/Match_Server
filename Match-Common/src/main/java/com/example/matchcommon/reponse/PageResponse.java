package com.example.matchcommon.reponse;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.*;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@Setter
@ToString
@Schema(description = "페이징 처리 응답")
public class PageResponse<T> implements Serializable {
    @Schema(description = "마지막 페이지 유무", required = true, example = "true")
    private final Boolean isLast;
    @Schema(description = "총 요소 갯수", required = true, example = "10")
    private final long totalCnt;
    @Schema(description = "요소", required = true)
    private final T contents;

    @JsonCreator
    public PageResponse(){
        this.isLast = null;
        this.totalCnt = 0;
        this.contents = null;
    }
}

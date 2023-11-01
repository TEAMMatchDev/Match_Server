package com.example.matchapi.common;

import com.example.matchapi.admin.event.service.AdminEventService;
import com.example.matchapi.common.model.UploadFolder;
import com.example.matchcommon.reponse.CommonResponse;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "IMG 컨트롤러")
@RestController
@RequestMapping("/admin/img")
@RequiredArgsConstructor
public class ImageController {
    private final S3UploadService s3UploadService;
    @PostMapping(value = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse<String> imgUpload(
            @ModelAttribute UploadFolder uploadFolder,
            @ModelAttribute MultipartFile imgFile){
        String url = s3UploadService.uploadOneImg(uploadFolder.getFolder(), imgFile);
        return CommonResponse.onSuccess(url);
    }
}

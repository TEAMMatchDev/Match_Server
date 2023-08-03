package com.example.adminapi.project.helper;

import com.example.matchcommon.annotation.Helper;
import com.example.matchinfrastructure.config.s3.S3UploadService;
import lombok.RequiredArgsConstructor;

@Helper
@RequiredArgsConstructor
public class ProjectHelper {
    private S3UploadService s3UploadService;


}

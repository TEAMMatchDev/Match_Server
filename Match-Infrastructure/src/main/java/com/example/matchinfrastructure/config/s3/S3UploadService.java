package com.example.matchinfrastructure.config.s3;


import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.example.matchcommon.exception.errorcode.FileUploadException.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.base-url}")
    private String baseUrl;




    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            throw new BadRequestException(FILE_UPLOAD_NOT_EMPTY);
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            throw new BadRequestException(FILE_UPLOAD_EXCEPTION);
        }
        System.out.println(idxFileName);
        return fileName.substring(fileName.lastIndexOf("."));
    }


    private String getForUserFileName(Long userId, String fileExtension) {
        return "user/"
                + userId.toString()
                + "/"
                + UUID.randomUUID()
                + fileExtension;
    }

    private String getForProjectFileName(Long projectId, String fileExtension) {
        return "project/"
                + projectId.toString()
                + "/"
                + UUID.randomUUID()
                + fileExtension;
    }

    public List<String> listUploadProjectFiles(Long projectId,List<MultipartFile> multipartFiles){
        List<String> imgUrlList = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            String fileName = getForProjectFileName(projectId, getFileExtension(file.getOriginalFilename()));
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
                imgUrlList.add(amazonS3.getUrl(bucket, fileName).toString());
            } catch (IOException e) {
                log.info("파일 업로드 실패 프로젝트 ID : " + projectId);
                throw new ForbiddenException(IMAGE_UPLOAD_ERROR);
            }
        }

        return imgUrlList;
    }


    private String changeJpgToJpeg(String fileExtension) {
        if (fileExtension.equals("jpg")) {
            return "jpeg";
        }
        return fileExtension;
    }

    public String uploadProjectPresentFile(Long projectId,MultipartFile presentFile) {
        String fileName = getForProjectFileName(projectId, getFileExtension(presentFile.getOriginalFilename()));
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(presentFile.getSize());
        objectMetadata.setContentType(presentFile.getContentType());

        try (InputStream inputStream = presentFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            log.info("파일 업로드 실패 프로젝트 ID : " + projectId);
            throw new ForbiddenException(IMAGE_UPLOAD_ERROR);
        }
        return amazonS3.getUrl(bucket, fileName).toString();
    }
}
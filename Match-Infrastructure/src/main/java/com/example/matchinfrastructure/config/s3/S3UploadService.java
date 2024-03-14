package com.example.matchinfrastructure.config.s3;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.matchcommon.exception.BadRequestException;
import com.example.matchcommon.exception.ForbiddenException;
import com.example.matchcommon.exception.InternalServerException;
import com.example.matchcommon.properties.AwsS3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.example.matchcommon.exception.errorcode.FileUploadException.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3UploadService {
    private static final String CLOUD_FRONT_DOMAIN_NAME = "https://d331gpen6ndprr.cloudfront.net";

    private final AmazonS3 amazonS3;
    private final AwsS3Properties awsS3Properties;


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
                amazonS3.putObject(new PutObjectRequest(awsS3Properties.getS3().getBucket(), fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
                imgUrlList.add(amazonS3.getUrl(awsS3Properties.getS3().getBucket(), fileName).toString());
            } catch (IOException e) {
                log.info("파일 업로드 실패 프로젝트 ID : " + projectId);
                throw new ForbiddenException(IMAGE_UPLOAD_ERROR);
            }
        }

        return imgUrlList;
    }

    public List<String> listUploadCompleteFiles(Long historyId,List<MultipartFile> multipartFiles){
        List<String> imgUrlList = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            String fileName = getForHistoryFileName(historyId, getFileExtension(file.getOriginalFilename()));
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                amazonS3.putObject(new PutObjectRequest(awsS3Properties.getS3().getBucket(), fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
                imgUrlList.add(getImageUrl(fileName));
            } catch (IOException e) {
                log.info("파일 업로드 실패 프로젝트 ID : " + historyId);
                throw new ForbiddenException(IMAGE_UPLOAD_ERROR);
            }
        }

        return imgUrlList;
    }

    private String getForHistoryFileName(Long historyId, String fileExtension) {
        return "history/"
                + historyId.toString()
                + "/"
                + UUID.randomUUID()
                + fileExtension;
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
            amazonS3.putObject(new PutObjectRequest(awsS3Properties.getS3().getBucket(), fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            log.info("파일 업로드 실패 프로젝트 ID : " + projectId);
            throw new ForbiddenException(IMAGE_UPLOAD_ERROR);
        }
        return getImageUrl(fileName);
    }

    @Async("delete-image")
    public void deleteFile(String fileName){
        int index=fileName.indexOf(CLOUD_FRONT_DOMAIN_NAME);
        String fileRoute=fileName.substring(index+CLOUD_FRONT_DOMAIN_NAME.length()+1);
        try {
            boolean isObjectExist = amazonS3.doesObjectExist(awsS3Properties.getS3().getBucket(), fileRoute);
            if (isObjectExist) {
                amazonS3.deleteObject(awsS3Properties.getS3().getBucket(),fileRoute);
            }
        } catch (Exception e) {
            throw new InternalServerException(IMAGE_DELETE_ERROR);
        }

    }

    public String uploadProfilePresentFile(Long userId,MultipartFile presentFile) {
        String fileName = getForUserFileName(userId, getFileExtension(presentFile.getOriginalFilename()));
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(presentFile.getSize());
        objectMetadata.setContentType(presentFile.getContentType());

        try (InputStream inputStream = presentFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(awsS3Properties.getS3().getBucket(), fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            log.info("파일 업로드 실패 프로젝트 ID : " + userId);
            throw new ForbiddenException(IMAGE_UPLOAD_ERROR);
        }
        return getImageUrl(fileName);
    }

    public String uploadBannerImage(MultipartFile bannerImage) {
        String fileName = getForBannerFileName(getFileExtension(bannerImage.getOriginalFilename()));
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(bannerImage.getSize());
        objectMetadata.setContentType(bannerImage.getContentType());

        try (InputStream inputStream = bannerImage.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(awsS3Properties.getS3().getBucket(), fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ForbiddenException(IMAGE_UPLOAD_ERROR);
        }
        return getImageUrl(fileName);
    }

    private String getForBannerFileName(String fileExtension) {
        return "banner/"
                + UUID.randomUUID()
                + fileExtension;
    }

    public String uploadByteCode(String s3FileName, byte[] thumbnailBytes) {
        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentLength(thumbnailBytes.length);
        metadata.setContentType("image/jpeg");

        amazonS3.putObject(new PutObjectRequest(awsS3Properties.getS3().getBucket(),s3FileName,new ByteArrayInputStream(thumbnailBytes),metadata));

        return getImageUrl(s3FileName);
    }


    public String uploadByteCodeOne(String dirName, byte[] thumbnailBytes) {
        String s3FileName=dirName+"/"+UUID.randomUUID().toString()+".jpg";

        return uploadByteCode(s3FileName, thumbnailBytes);
    }

    public String uploadOneImg(String dirName, MultipartFile imgFile) {
        String fileName = getForOneFileName(dirName, getFileExtension(Objects.requireNonNull(imgFile.getOriginalFilename())));

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(imgFile.getSize());
        objectMetadata.setContentType(imgFile.getContentType());

        try (InputStream inputStream = imgFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(awsS3Properties.getS3().getBucket(), fileName, inputStream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ForbiddenException(IMAGE_UPLOAD_ERROR);
        }
        return getImageUrl(fileName);
    }

    private String getForOneFileName(String dirName, String fileExtension) {
        return  dirName+"/"
                + UUID.randomUUID()
                + fileExtension;
    }

    private String getImageUrl(String fileName){
        return CLOUD_FRONT_DOMAIN_NAME + "/" + fileName;
    }
}

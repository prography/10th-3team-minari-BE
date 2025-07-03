package com.prography.minari.aws.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader implements FileUploader {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /****
     * Uploads a multipart file to the specified folder path within the configured AWS S3 bucket.
     *
     * @param file the multipart file to upload
     * @param relativeFolderPath the folder path within the S3 bucket where the file will be stored
     * @return the HTTPS URL of the uploaded file in the S3 bucket
     * @throws ApiException if an I/O error occurs during file upload
     */
    @Override
    public String upload(MultipartFile file, String relativeFolderPath) {
        String fileName = file.getOriginalFilename();
        String fullPath = relativeFolderPath + "/" + fileName;
        String fileUrl = "https://" + bucket + fullPath;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        try {
            amazonS3Client.putObject(bucket, fullPath, file.getInputStream(), metadata);
        } catch (IOException e) {
            log.error("Failed to upload file", e.getStackTrace()[0]);
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        return fileUrl;
    }
}

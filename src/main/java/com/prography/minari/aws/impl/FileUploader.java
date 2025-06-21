package com.prography.minari.aws.impl;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploader {
    String upload(MultipartFile multipartFile,String relativeFolderPath);
}

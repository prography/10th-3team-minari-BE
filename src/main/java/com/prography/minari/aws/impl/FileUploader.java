package com.prography.minari.aws.impl;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploader {
    /**
 * Uploads a file to a specified relative folder path and returns the resulting file location or identifier.
 *
 * @param multipartFile the file to be uploaded
 * @param relativeFolderPath the relative path within the storage destination where the file should be placed
 * @return the location or identifier of the uploaded file
 */
String upload(MultipartFile multipartFile,String relativeFolderPath);
}

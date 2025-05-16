package com.prography.minari.answer.service.impl;

import org.springframework.web.multipart.MultipartFile;

public interface SttApiClient {
    String convertToText(MultipartFile file);
}

package com.prography.minari.answer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SttProcessingService {
    private final SttApiClient sttApiClient;

    public String convertToText(MultipartFile file) {
        return sttApiClient.convertToText(file);
    }
}

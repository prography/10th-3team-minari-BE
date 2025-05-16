package com.prography.minari.answer.service.impl;

import com.prography.minari.common.aop.ImplService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@ImplService
@RequiredArgsConstructor
public class SttProcessor {
    private final SttApiClient sttApiClient;

    public String convertToText(MultipartFile file) {
        return sttApiClient.convertToText(file);
    }
}

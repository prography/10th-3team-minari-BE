package com.prography.minari.answer.service.impl;

import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface SttApiClient {
    String convertToText(byte[] file); // 동기용
    Mono<String> convertToTextNonBlock(byte[] file); // WebClient 전용
}

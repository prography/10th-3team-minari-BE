package com.prography.minari.answer.service.impl;


import com.prography.minari.answer.service.dto.response.NaverSttApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Component
public class NaverApiClient implements SttApiClient {
    private WebClient webClient;

    @Value("${naver.client.id:default}")
    private String clientId;
    @Value("${naver.client.secret:default}")
    private String clientSecret;

    public NaverApiClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://naveropenapi.apigw.ntruss.com")
                .build();
    }


    @Override
    public String convertToText(MultipartFile voiceFile) {
        try {
            NaverSttApiResponse response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/recog/v1/stt")
                            .queryParam("lang", "Kor")
                            .build())
                    .header("X-NCP-APIGW-API-KEY-ID", clientId)
                    .header("X-NCP-APIGW-API-KEY", clientSecret)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .bodyValue(voiceFile.getBytes())
                    .retrieve()
                    .bodyToMono(NaverSttApiResponse.class)
                    .block();
            return Objects.requireNonNull(response).getText();
        } catch (Exception e) {
            throw new RuntimeException("STT API 호출 실패", e);
        }
    }
}

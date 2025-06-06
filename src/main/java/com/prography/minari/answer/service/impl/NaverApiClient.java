package com.prography.minari.answer.service.impl;


import com.prography.minari.answer.service.dto.SttResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
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
    public String convertToText(byte[] voiceFile) {
        try {
            log.info("Converting text to Naver");
            SttResponseDto.Naver block = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/recog/v1/stt")
                            .queryParam("lang", "Kor")
                            .build())
                    .header("X-NCP-APIGW-API-KEY-ID", clientId)
                    .header("X-NCP-APIGW-API-KEY", clientSecret)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .bodyValue(voiceFile)
                    .retrieve()
                    .bodyToMono(SttResponseDto.Naver.class)
                    .block();
            return block.getText();
        } catch (Exception e) {
            throw new RuntimeException("STT API 호출 실패", e);
        }
    }
}

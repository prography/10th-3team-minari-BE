package com.prography.minari.answer.service.impl;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SttApiClientTest {

    private MockWebServer mockWebServer;
    private SttApiClient sttApiClient;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String baseUrl = mockWebServer.url("/").toString();
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();

        NaverApiClient naverApiClient = new NaverApiClient();
        ReflectionTestUtils.setField(naverApiClient, "clientId", "test-client-id");
        ReflectionTestUtils.setField(naverApiClient, "clientSecret", "test-client-secret");
        ReflectionTestUtils.setField(naverApiClient, "webClient", webClient);

        this.sttApiClient = naverApiClient;
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @DisplayName("STT api 호출 성공 테스트")
    @Test
    void convertToText_shouldReturnRecognizedText() throws IOException {
        // given
        MultipartFile mockFile = new MockMultipartFile(
                "file", "test.wav", "audio/wav", "dummy audio".getBytes()
        );
        String expectedText = "{\"text\":\"테스트입니다\"}";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setHeader("X-NCP-APIGW-API-KEY-ID", "test_id")
                .setHeader("X-NCP-APIGW-API-KEY", "test-secret")
                .setBody(expectedText));

        // when
        String result = sttApiClient.convertToText(mockFile);

        // then
        assertEquals(expectedText, result);
    }

    @Test
    @DisplayName("")
    void STT호출테스트_API_500_서버에러_테스트() {
        // given

        // when

        // then
    }

    @Test
    @DisplayName("")
    void STT호출테스트_API_400_서버에러_테스트() {
        // given
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"error\": \"잘못된 요청입니다\"}"));

        MultipartFile mockFile = new MockMultipartFile(
                "file", "test.wav", "audio/wav", "dummy audio".getBytes()
        );

        // when & then
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                sttApiClient.convertToText(mockFile)
        );
        assertTrue(ex.getMessage().contains("STT API 호출 "));
    }
}
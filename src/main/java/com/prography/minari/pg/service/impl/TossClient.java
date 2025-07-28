package com.prography.minari.pg.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.common.util.TossUtil;
import com.prography.minari.pg.dto.PaymentResponse;
import com.prography.minari.pg.dto.TossPaymentConfirm.TossPaymentConfirmRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@ImplService
public class TossClient {

    @Value("${toss.base-url:default}")
    private String BASE_URL;

    @Value("${toss.secret:default}")
    private String SECRET;

    public PaymentResponse confirmPayment(String paymentKey, BigDecimal amount, Long userId, Long productId) {
        String orderId = TossUtil.generateUUID(userId, productId);

        return generateBasicWebClient()
                .post()
                .uri("/v1/payments/confirm")
                .bodyValue(TossPaymentConfirmRequest.from(paymentKey, orderId, amount))
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .block();
    }

    private WebClient generateBasicWebClient() {
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + SECRET)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}

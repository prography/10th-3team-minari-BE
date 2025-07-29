package com.prography.minari.pg.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.minari.common.aop.ImplService;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.common.execption.TossApiException;
import com.prography.minari.common.util.TossUtil;
import com.prography.minari.pg.dto.PaymentErrorResponse;
import com.prography.minari.pg.dto.PaymentResponse;
import com.prography.minari.pg.dto.TossPaymentConfirm.TossPaymentConfirmRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
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
                .onStatus(status -> status.isError(), clientResponse ->
                        clientResponse.bodyToMono(PaymentErrorResponse.class)
                                .flatMap(errorBody -> {
                                    log.info("Toss Error Response: {}", errorBody);
                                    return Mono.error(new TossApiException(errorBody.code(), errorBody.message()));
                                })
                )
                .bodyToMono(PaymentResponse.class)
                .block();
    }

    private WebClient generateBasicWebClient() {
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, TossUtil.encodedSecret(SECRET))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}

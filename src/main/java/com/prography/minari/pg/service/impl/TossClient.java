package com.prography.minari.pg.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.common.execption.TossApiException;
import com.prography.minari.common.util.TossUtil;
import com.prography.minari.pg.dto.TossErrorResponse;
import com.prography.minari.pg.dto.common.PaymentResponse;
import com.prography.minari.pg.dto.TossPaymentCancel.TossPaymentCancelReqDto;
import com.prography.minari.pg.dto.TossPaymentConfirm.TossPaymentConfirmRequest;
import com.prography.minari.pg.dto.common.TransactionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Slf4j
@ImplService
public class TossClient {

    @Value("${toss.base-url:default}")
    private String BASE_URL;

    @Value("${toss.secret:default}")
    private String SECRET;

    public PaymentResponse confirmPayment(String paymentKey, String orderId, BigDecimal amount) {
        return retirevePostRequest(
                "/v1/payments/confirm",
                TossPaymentConfirmRequest.from(paymentKey, orderId, amount),
                PaymentResponse.class
        );
    }

    public void getPaymentByPaymentKey(String paymentKey) {
        retireveGetRequest(
                "/v1/payments/{paymentKey}",
                paymentKey,
                PaymentResponse.class
        );
    }

    public void getPaymentByOrderId(String orderId) {
        retireveGetRequest(
                "/v1/payments/orders/{orderId}",
                orderId,
                PaymentResponse.class
        );
    }

    public void cancelPayment(String paymentKey, TossPaymentCancelReqDto reqDto) {
        retirevePostRequest(
                "/v1/payments/{paymentKey}/cancel",
                paymentKey,
                reqDto,
                PaymentResponse.class
        );
    }

    public void getTransactionList(ZonedDateTime startDate, ZonedDateTime endDate, String startingAfter, int limit) {
        generateBasicWebClient()
                .get()
                .uri("/v1/transactions")
                .retrieve()
                .bodyToFlux(TransactionResponse.class)
                .collectList();
    }

    private WebClient generateBasicWebClient() {
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, SECRET)//TossUtil.encodedSecret(SECRET))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private <T>T retireveGetRequest(String uri, Class<T> classType) {
        return generateBasicWebClient()
                .get()
                .uri(uri)
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse ->
                        clientResponse.bodyToMono(TossErrorResponse.class)
                                .flatMap(errorBody -> {
                                    log.info("Toss Error Response: {}", errorBody);
                                    return Mono.error(new TossApiException(errorBody.code(), errorBody.message()));
                                })
                )
                .bodyToMono(classType)
                .block();

    }

    private <T>T retireveGetRequest(String uri, Object pathVar, Class<T> classType) {
        return generateBasicWebClient()
                .get()
                .uri(uri, pathVar)
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse ->
                        clientResponse.bodyToMono(TossErrorResponse.class)
                                .flatMap(errorBody -> {
                                    log.info("Toss Error Response: {}", errorBody);
                                    return Mono.error(new TossApiException(errorBody.code(), errorBody.message()));
                                })
                )
                .bodyToMono(classType)
                .block();

    }

    private <T>T retirevePostRequest(String uri, Object body, Class<T> classType) {
        return generateBasicWebClient()
                .post()
                .uri(uri)
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse ->
                        clientResponse.bodyToMono(TossErrorResponse.class)
                                .flatMap(errorBody -> {
                                    log.info("Toss Error Response: {}", errorBody);
                                    return Mono.error(new TossApiException(errorBody.code(), errorBody.message()));
                                })
                )
                .bodyToMono(classType)
                .block();

    }

    private <T>T retirevePostRequest(String uri, Object pathVar, Object body, Class<T> classType) {
        return generateBasicWebClient()
                .post()
                .uri(uri, pathVar)
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> status.isError(), clientResponse ->
                        clientResponse.bodyToMono(TossErrorResponse.class)
                                .flatMap(errorBody -> {
                                    log.info("Toss Error Response: {}", errorBody);
                                    return Mono.error(new TossApiException(errorBody.code(), errorBody.message()));
                                })
                )
                .bodyToMono(classType)
                .block();

    }

}

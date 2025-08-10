package com.prography.minari.pg.service;

import com.prography.minari.common.service.impl.RedisProcessor;
import com.prography.minari.payment.service.impl.ProductReader;
import com.prography.minari.pg.dto.common.PaymentResponse;
import com.prography.minari.pg.dto.TossPaymentCancel.TossPaymentCancelReqDto;
import com.prography.minari.pg.dto.TossPaymentConfirm.TossPaymentConfirmReqDto;
import com.prography.minari.pg.repository.TossPaymentRepository;
import com.prography.minari.pg.service.impl.TossClient;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TossService {

    private final TossClient tossClient;
    private final RedisProcessor redisProcessor;
    private final TossPaymentRepository tossPaymentRepository;

    private final ProductReader productReader;

    @Transactional
    public void confirm(String paymentKey, String orderId, BigDecimal amount, User user) {

        /*

        // 상품 ID와 일치하는 상품이 존재하지 않을 경우, 예외처리
        Product product = productReader.read(reqDto.productId())
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCTION_NOT_FOUND));

        // 상품의 가격과 사용자가 지불하는 비용이 일치하지 않을 경우, 예외처리
        if (BigDecimal.valueOf(product.getRealPrice()).compareTo(reqDto.amount()) != 0)
            throw new ApiException(INVALID_PRICE_MISMATCH);

        */

        // TOSS 결제 승인 API 호출 -  https://api.tosspayments.com/v1/payments/confirm
        PaymentResponse paymentResponse = tossClient.confirmPayment(paymentKey, orderId, amount);

        /*
        // TOSS 결제 승인 Response 저장
        tossPaymentRepository.save(PaymentResponse.from(paymentResponse));
        */
    }

    public void getPaymentByPaymentKey(String paymentKey) {
        tossClient.getPaymentByPaymentKey(paymentKey);
    }

    public void getPaymentByOrderId(String orderId) {
        tossClient.getPaymentByOrderId(orderId);
    }

    public void cancelPayment(String paymentKey, TossPaymentCancelReqDto reqDto) {
        tossClient.cancelPayment(paymentKey, reqDto);
    }

    public void getTransactionList(ZonedDateTime startDate, ZonedDateTime endDate, String startingAfter, int limit) {
        tossClient.getTransactionList(startDate, endDate, startingAfter, limit);
    }

    public void prepare(String orderId, BigDecimal amount) {
        redisProcessor.
    }

}

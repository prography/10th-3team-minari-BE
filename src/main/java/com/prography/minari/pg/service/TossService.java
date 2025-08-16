package com.prography.minari.pg.service;

import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.service.impl.RedisProcessor;
import com.prography.minari.common.util.TossUtil;
import com.prography.minari.payment.entity.Credit;
import com.prography.minari.payment.entity.Product;
import com.prography.minari.payment.service.impl.CreditWriter;
import com.prography.minari.payment.service.impl.ProductReader;
import com.prography.minari.pg.dto.TossPaymentPrepare.TossPaymentPrepareReqDto;
import com.prography.minari.pg.dto.common.Payment;
import com.prography.minari.pg.dto.common.PaymentResponse;
import com.prography.minari.pg.dto.TossPaymentCancel.TossPaymentCancelReqDto;
import com.prography.minari.pg.dto.TossPaymentConfirm.TossPaymentConfirmReqDto;
import com.prography.minari.pg.repository.TossPaymentRepository;
import com.prography.minari.pg.service.impl.TossClient;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;

import static com.prography.minari.common.execption.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TossService {

    private final TossClient tossClient;
    private final RedisProcessor redisProcessor;

    private final ProductReader productReader;
    private final CreditWriter creditWriter;

    @Transactional
    public Payment confirm(TossPaymentConfirmReqDto reqDto, User user) {

        BigDecimal amount  = reqDto.amount();
        String paymentKey  = reqDto.paymentKey();
        String orderId     = reqDto.orderId();
        Long productId     = reqDto.productId();
        Long userId        = user.getId();

        // orderId에 해당하는 amount가 존재하지 않거나 amount가 일치하지 않을 경우, 예외처리
        redisProcessor.getValue(reqDto.orderId())
                .map(Object::toString)
                .map(BigDecimal::new)
                .filter(prepareAmount -> prepareAmount.compareTo(amount) == 0) // 금액 동일할 때만 통과
                .orElseThrow(() -> new ApiException(INVALID_PRICE_MISMATCH));

        // TOSS 결제 승인 API 호출 -  https://api.tosspayments.com/v1/payments/confirm
        Payment payment = tossClient.confirmPayment(paymentKey, orderId, amount);
        log.info("payment confirm request  : {}", reqDto);
        log.info("payment confirm response : {}", payment);

        // Credit 저장
        creditWriter.write(Credit.create(amount.longValue(), userId, Long.valueOf(paymentKey), productId));

        return payment;
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

    public void prepare(TossPaymentPrepareReqDto reqDto) {

         // 상품 ID와 일치하는 상품이 존재하지 않을 경우, 예외처리
         Product product = productReader.read(reqDto.productId())
                 .orElseThrow(() -> new ApiException(PRODUCTION_NOT_FOUND));

        // 상품의 가격과 사용자가 지불하는 비용이 일치하지 않을 경우, 예외처리
         if (BigDecimal.valueOf(product.getRealPrice()).compareTo(reqDto.amount()) != 0)
             throw new ApiException(INVALID_PRICE_MISMATCH);

         // 결제 금액이 100,000원을 초과할 경우, 예외처리
         if(TossUtil.isGreaterThan(reqDto.amount())) {
             throw new ApiException(INVALID_AMOUNT);
         }

        redisProcessor.setValue(reqDto.orderId(), String.valueOf(reqDto.amount()), Duration.ofMinutes(10));
    }

}

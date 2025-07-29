package com.prography.minari.pg.service;

import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.payment.entity.Product;
import com.prography.minari.payment.service.impl.ProductReader;
import com.prography.minari.pg.dto.PaymentResponse;
import com.prography.minari.pg.dto.TossPaymentConfirmReqtDto;
import com.prography.minari.pg.repository.TossPaymentRepository;
import com.prography.minari.pg.service.impl.TossClient;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.prography.minari.common.execption.ErrorCode.INVALID_PRICE_MISMATCH;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TossService {

    private final TossClient tossClient;
    private final TossPaymentRepository tossPaymentRepository;

    private final ProductReader productReader;

    @Transactional
    public void confirm(TossPaymentConfirmReqtDto reqDto, User user) {

        String paymentKey = reqDto.paymentKey();
        BigDecimal amount = reqDto.amount();
        Long productId    = reqDto.productId();
        Long userId       = 1L;

        /*

        // 상품 ID와 일치하는 상품이 존재하지 않을 경우, 예외처리
        Product product = productReader.read(reqDto.productId())
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCTION_NOT_FOUND));

        // 상품의 가격과 사용자가 지불하는 비용이 일치하지 않을 경우, 예외처리
        if (BigDecimal.valueOf(product.getRealPrice()).compareTo(reqDto.amount()) != 0)
            throw new ApiException(INVALID_PRICE_MISMATCH);

        */

        // TOSS 결제 승인 API 호출 -  https://api.tosspayments.com/v1/payments/confirm
        PaymentResponse paymentResponse = tossClient.confirmPayment(paymentKey, amount, userId, productId);

        /*
        // TOSS 결제 승인 Response 저장
        tossPaymentRepository.save(PaymentResponse.from(paymentResponse));
        */
    }

}

package com.prography.minari.pg.service;

import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.payment.entity.Product;
import com.prography.minari.payment.service.impl.ProductReader;
import com.prography.minari.pg.dto.PaymentResponse;
import com.prography.minari.pg.dto.TossPaymentConfirmReqtDto;
import com.prography.minari.pg.entity.TossPayment;
import com.prography.minari.pg.repository.TossPaymentRepository;
import com.prography.minari.pg.service.impl.TossClient;
import com.prography.minari.user.entity.User;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static com.prography.minari.common.execption.ErrorCode.INVALID_PRICE_MISMATCH;
import static com.prography.minari.common.util.TossUtil.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TossService {

    private final TossClient tossClient;
    private final TossPaymentRepository tossPaymentRepository;

    private final ProductReader productReader;

    @Transactional
    public void confirm(TossPaymentConfirmReqtDto reqDto, User user) {

        // 상품 ID와 일치하는 상품이 존재하지 않을 경우, 예외처리
        Product product = productReader.read(reqDto.productId())
                .orElseThrow(() -> new ApiException(ErrorCode.ENTITY_NOT_FOUND));

        // 상품의 가격과 사용자가 지불하는 비용이 일치하지 않을 경우, 예외처리
        if (BigDecimal.valueOf(product.getRealPrice()).compareTo(reqDto.amount()) != 0)
            throw new ApiException(INVALID_PRICE_MISMATCH);

        // TOSS 결제 승인 API 호출
        PaymentResponse paymentResponse = tossClient.confirmPayment(reqDto.paymentKey(), reqDto.amount(), user.getId(), reqDto.productId());

        // TOSS 결제 승인 Response 저장
        tossPaymentRepository.save(PaymentResponse.from(paymentResponse));

    }

}

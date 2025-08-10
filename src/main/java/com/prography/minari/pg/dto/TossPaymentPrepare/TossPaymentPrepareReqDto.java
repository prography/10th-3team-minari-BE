package com.prography.minari.pg.dto.TossPaymentPrepare;

import java.math.BigDecimal;

public record TossPaymentPrepareReqDto(
        Long productId,
        String orderId,
        BigDecimal amount
) {
}

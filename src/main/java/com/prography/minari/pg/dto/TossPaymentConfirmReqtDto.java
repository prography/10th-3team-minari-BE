package com.prography.minari.pg.dto;

import java.math.BigDecimal;

public record TossPaymentConfirmReqtDto(
        String paymentKey,
        BigDecimal amount,
        Long productId
) {
}

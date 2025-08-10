package com.prography.minari.pg.dto.TossPaymentConfirm;

import java.math.BigDecimal;

public record TossPaymentConfirmReqDto(
        String paymentKey,
        BigDecimal amount,
        Long productId
) {
}

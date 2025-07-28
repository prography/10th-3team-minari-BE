package com.prography.minari.pg.dto.TossPaymentConfirm;

import java.math.BigDecimal;

public record TossPaymentConfirmRequest(
        String paymentKey,
        String orderId,
        BigDecimal amount
) {
    public static TossPaymentConfirmRequest from(String paymentKey, String orderId, BigDecimal amount) {
        return new TossPaymentConfirmRequest(paymentKey, orderId, amount);
    }
}

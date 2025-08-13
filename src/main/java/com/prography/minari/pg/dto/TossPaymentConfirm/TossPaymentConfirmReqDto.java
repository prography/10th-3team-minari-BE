package com.prography.minari.pg.dto.TossPaymentConfirm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TossPaymentConfirmReqDto(
        @NotBlank String paymentKey,
        @NotBlank String orderId,
        @NotNull BigDecimal amount,
        @NotNull Long productId
) {
}

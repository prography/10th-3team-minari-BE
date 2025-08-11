package com.prography.minari.pg.dto.TossPaymentPrepare;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TossPaymentPrepareReqDto(
        @NotNull Long productId,
        @NotBlank String orderId,
        @NotNull BigDecimal amount
) {
}

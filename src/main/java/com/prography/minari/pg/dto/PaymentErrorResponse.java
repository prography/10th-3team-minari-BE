package com.prography.minari.pg.dto;

public record PaymentErrorResponse(
        String code,
        String message
) {
}

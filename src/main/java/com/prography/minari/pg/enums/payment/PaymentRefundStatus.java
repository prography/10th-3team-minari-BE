package com.prography.minari.pg.enums.payment;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentRefundStatus {
    NONE,
    PENDING,
    FAILED,
    PARTIAL_FAILED,
    COMPLETED,
    UNKNOWN;

    @JsonCreator
    public static PaymentRefundStatus from(String value) {
        try {
            return PaymentRefundStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}

package com.prography.minari.pg.enums.payment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentStatus {

    READY,
    IN_PROGRESS,
    WAITING_FOR_DEPOSIT,
    DONE,
    CANCELED,
    PARTIAL_CANCELED,
    ABORTED,
    EXPIRED,
    UNKNOWN;

    @JsonCreator
    public static PaymentStatus from(String value) {
        try {
            return PaymentStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}

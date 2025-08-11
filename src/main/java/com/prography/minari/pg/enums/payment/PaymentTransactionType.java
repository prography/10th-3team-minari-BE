package com.prography.minari.pg.enums.payment;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentTransactionType {
    CONFIRM,
    CANCEL,
    UNKNOWN;

    @JsonCreator
    public static PaymentTransactionType from(String value) {
        try {
            return PaymentTransactionType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}

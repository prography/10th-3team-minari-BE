package com.prography.minari.pg.enums.payment;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentType {
    NORMAL,
    BILLING,
    BRANDPAY,
    UNKNOWN;

    @JsonCreator
    public static PaymentType from(String value) {
        try {
            return PaymentType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}

package com.prography.minari.pg.enums.payment;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentSettlementStatus {
    INCOMPLETED,
    COMPLETED,
    UNKNOWN;

    @JsonCreator
    public static PaymentSettlementStatus from(String value) {
        try {
            return PaymentSettlementStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}

package com.prography.minari.pg.enums.payment;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentIssueStatus {
    IN_PROGRESS,
    COMPLETED,
    FAILED,
    UNKNOWN;

    @JsonCreator
    public static PaymentIssueStatus from(String value) {
        try {
            return PaymentIssueStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

}

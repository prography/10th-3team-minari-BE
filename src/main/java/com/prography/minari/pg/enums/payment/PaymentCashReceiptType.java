package com.prography.minari.pg.enums.payment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;
import java.util.Map;

public enum PaymentCashReceiptType {
    INCOME_DEDUCTION("소득공제"),
    EXPENSE_PROOF("지출증빙"),
    UNKNOWN("알수없음");

    private final String label;
    PaymentCashReceiptType(String label) { this.label = label; }

    @JsonValue
    public String toValue() { return label; }

    private static final Map<String, PaymentCashReceiptType> BY_LABEL = Map.of(
            "소득공제", INCOME_DEDUCTION,
            "지출증빙", EXPENSE_PROOF
    );

    @JsonCreator
    public static PaymentCashReceiptType from(String value) {
        if (value == null) return UNKNOWN;
        // 한글 라벨 우선 매핑
        PaymentCashReceiptType byKo = BY_LABEL.get(value.trim());
        if (byKo != null) return byKo;
        // 혹시 영문 코드가 들어와도 방어
        try { return PaymentCashReceiptType.valueOf(value.trim().toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException e) { return UNKNOWN; }
    }
}

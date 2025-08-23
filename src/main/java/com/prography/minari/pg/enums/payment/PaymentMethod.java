package com.prography.minari.pg.enums.payment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentMethod {
    CARD("카드"),
    VIRTUAL_ACCOUNT("가상계좌"),
    SIMPLE_PAY("간편결제"),
    MOBILE("휴대폰"),
    BANK_TRANSFER("계좌이체"),
    CULTURE_GIFT("문화상품권"),
    BOOK_GIFT("도서문화상품권"),
    GAME_GIFT("게임문화상품권"),
    UNKNOWN("알수없음");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // 한글값 → Enum 변환
    @JsonCreator
    public static PaymentMethod from(String value) {
        for (PaymentMethod method : values()) {
            if (method.value.equals(value)) {
                return method;
            }
        }
        return UNKNOWN;
    }

    // Enum → 한글값 변환
    @JsonValue
    public String toValue() {
        return this.value;
    }
}

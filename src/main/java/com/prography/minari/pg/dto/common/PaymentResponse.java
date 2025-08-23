package com.prography.minari.pg.dto.common;


import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PaymentResponse(
        String version,
        String paymentKey,
        String type,
        String orderId,
        String orderName,
        String mId,
        String currency,
        String method,
        BigDecimal totalAmount,
        BigDecimal balanceAmount,
        String status,
        OffsetDateTime requestedAt,
        OffsetDateTime approvedAt,
        boolean useEscrow,
        boolean cultureExpense,
        boolean isPartialCancelable,
        BigDecimal suppliedAmount,
        BigDecimal vat,
        BigDecimal taxFreeAmount,
        BigDecimal taxExemptionAmount,
        String lastTransactionKey,
        String country,
        CardInfo card,
        EasyPayInfo easyPay,
        ReceiptInfo receipt,
        CheckoutInfo checkout
) {
    public record CardInfo(
            String issuerCode,
            String acquirerCode,
            String number,
            int installmentPlanMonths,
            boolean isInterestFree,
            String interestPayer,
            String approveNo,
            boolean useCardPoint,
            String cardType,
            String ownerType,
            String acquireStatus,
            BigDecimal amount
    ) {}

    public record EasyPayInfo(
            String provider,
            BigDecimal amount,
            BigDecimal discountAmount
    ) {}

    public record ReceiptInfo(
            String url
    ) {}

    public record CheckoutInfo(
            String url
    ) {}

}


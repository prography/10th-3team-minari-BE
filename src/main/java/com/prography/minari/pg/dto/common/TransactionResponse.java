package com.prography.minari.pg.dto.common;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record TransactionResponse(
        String mId,
        String transactionKey,
        String paymentKey,
        String orderId,
        String method,
        String customerKey,
        boolean useEscrow,
        String receiptUrl,
        String status,
        ZonedDateTime transactionAt,
        String currency,
        BigDecimal amount
) {
}
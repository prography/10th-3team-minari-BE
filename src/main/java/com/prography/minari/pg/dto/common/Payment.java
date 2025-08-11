package com.prography.minari.pg.dto.common;

import com.prography.minari.pg.enums.payment.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record Payment(
        String version,    //
        String paymentKey, // 결제의 키값
        PaymentType type,  //
        String orderId,
        String orderName,
        String mId,
        String currency,
        PaymentMethod method,
        BigDecimal totalAmount,
        BigDecimal balanceAmount,
        PaymentStatus status,
        OffsetDateTime requestedAt,
        OffsetDateTime approvedAt,
        boolean useEscrow,
        String lastTransactionKey,
        BigDecimal suppliedAmount,
        BigDecimal vat,
        boolean cultureExpense,
        BigDecimal taxFreeAmount,
        BigDecimal taxExemptionAmount,
        List<Cancel> cancels,
        boolean isPartialCancelable,
        Card card,
        VirtualAccount virtualAccount,
        String secret,
        MobilePhone mobilePhone,
        GiftCertificate giftCertificate,
        Transfer transfer,
        Map<String, String> metadata,
        Receipt receipt,
        Checkout checkout,
        EasyPay easyPay,
        String country,
        Failure failure,
        CashReceipt cashReceipt,
        CashReceipts cashReceipts,
        Discount discount
) {
    public record Cancel(
        BigDecimal cancelAmount,           // 취소 금액
        String cancelReason,               // 취소 사유 (최대 200자)
        BigDecimal taxFreeAmount,          // 면세 금액
        Integer taxExemptionAmount,        // 과세 제외 금액
        BigDecimal refundableAmount,       // 환불 가능 잔액
        BigDecimal cardDiscountAmount,     // 카드 즉시할인 취소 금액
        BigDecimal transferDiscountAmount, // 퀵계좌이체 즉시할인 취소 금액
        BigDecimal easyPayDiscountAmount,  // 간편결제 적립식 취소 금액
        OffsetDateTime canceledAt,         // 취소 시각 (ISO 8601)
        String transactionKey,             // 취소 거래 키 (최대 64자)
        String receiptKey,                 // 현금영수증 키 (nullable)
        String cancelStatus,               // 취소 상태 (예: DONE)
        String cancelRequestId             // 비동기 결제 전용 (nullable)
    ) {}

    public record Card(
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

    public record VirtualAccount(
        String accountType,
        String accountNumber,
        String bankCode,
        String customerName,
        String depositorName,
        OffsetDateTime dueDate,
        PaymentRefundStatus refundStatus,
        boolean expired,
        PaymentSettlementStatus settlementStatus,
        RefundReceiveAccount refundReceiveAccount
    ){
        public record RefundReceiveAccount(
                String bankCode,
                String accountNumber,
                String holderName
        ){}
    }

    public record MobilePhone(
        String customerMobilePhone,
        String settlementStatus,
        String receiptUrl
    ) {}

    public record GiftCertificate(
        String approveNo,
        PaymentSettlementStatus settlementStatus
    ) {}

    public record Transfer(
        String bankCode,
        PaymentSettlementStatus settlementStatus
    ) {}

    public record Receipt(
            String url
    ) {}

    public record Checkout(
            String url
    ) {}

    public record EasyPay(
        String provider,
        BigDecimal amount,
        BigDecimal discountAmount
    ) {}

    public record Failure(
        String code,
        String message
    ) {}

    public record CashReceipt(
        PaymentCashReceiptType type,
        String receiptKey,
        String issueNumber,
        String receiptUrl,
        BigDecimal amount,
        BigDecimal taxFreeAmount
    ) {}

    public record CashReceipts(
        String receiptKey,
        String orderId,
        String orderName,
        PaymentCashReceiptType type,
        String issueNumber,
        String receiptUrl,
        String businessNumber,
        PaymentTransactionType transactionType,
        BigDecimal amount,
        BigDecimal taxFreeAmount,
        PaymentIssueStatus issueStatus,
        Failure failure,
        String customerIdentityNumber,
        OffsetDateTime requestedAt
    ) {
        public record Failure(
                String code,
                String message
        ) {}
    }

    public record Discount(
        BigDecimal amount
    ) {}


}

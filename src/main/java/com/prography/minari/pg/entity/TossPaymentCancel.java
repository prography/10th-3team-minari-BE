package com.prography.minari.pg.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Getter
@Table(name = "toss_payment_cancels")
@NoArgsConstructor
public class TossPaymentCancel {
    @Id
    private String paymentKey;                 // PK
    private String cancelReason;               // 취소 사유 (최대 200자)
    private String transactionKey;             // 취소 거래 키 (최대 64자)
    private String receiptKey;                 // 현금영수증 키 (nullable)
    private String cancelStatus;               // 취소 상태 (예: DONE)
    private String cancelRequestId;            // 비동기 결제 전용 (nullable)
    private Integer taxExemptionAmount;        // 과세 제외 금액
    private BigDecimal cancelAmount;           // 취소 금액
    private BigDecimal taxFreeAmount;          // 면세 금액
    private BigDecimal refundableAmount;       // 환불 가능 잔액
    private BigDecimal cardDiscountAmount;     // 카드 즉시할인 취소 금액
    private BigDecimal transferDiscountAmount; // 퀵계좌이체 즉시할인 취소 금액
    private BigDecimal easyPayDiscountAmount;  // 간편결제 적립식 취소 금액
    private OffsetDateTime canceledAt;         // 취소 시각 (ISO 8601)
}

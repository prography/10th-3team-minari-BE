package com.prography.minari.pg.entity;

import com.prography.minari.pg.dto.common.Payment;
import com.prography.minari.pg.enums.payment.PaymentCashReceiptType;
import com.prography.minari.pg.enums.payment.PaymentIssueStatus;
import com.prography.minari.pg.enums.payment.PaymentTransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "toss_payment_cash_receipts")
public class TossPaymentCashReceipts {
    
    @Id
    private String paymentKey;
    private Integer seq;

    private String receiptKey;
    private String orderId;
    private String orderName;
    private PaymentCashReceiptType type;
    private String issueNumber;
    private String receiptUrl;
    private String businessNumber;
    private PaymentTransactionType transactionType;
    private BigDecimal amount;
    private BigDecimal taxFreeAmount;
    private PaymentIssueStatus issueStatus;
    private String failureCode;
    private String failureMessage;
    private String customerIdentityNumber;
    private OffsetDateTime requestedAt;
    
}

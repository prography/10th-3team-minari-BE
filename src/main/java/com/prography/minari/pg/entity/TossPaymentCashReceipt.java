package com.prography.minari.pg.entity;

import com.prography.minari.pg.enums.payment.PaymentCashReceiptType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "toss_payment_cash_receipt")
public class TossPaymentCashReceipt {
    
    @Id
    private String paymentKey;
    private PaymentCashReceiptType type;
    private String receiptKey;
    private String issueNumber;
    private String receiptUrl;
    private BigDecimal amount;
    private BigDecimal taxFreeAmount;
    
}

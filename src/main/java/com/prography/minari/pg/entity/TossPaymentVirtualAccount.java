package com.prography.minari.pg.entity;

import com.prography.minari.pg.enums.payment.PaymentRefundStatus;
import com.prography.minari.pg.enums.payment.PaymentSettlementStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Getter
@Table(name = "toss_payment_virtual_accounts")
@NoArgsConstructor
public class TossPaymentVirtualAccount {
    private String paymentKey;
    private String accountType;
    private String accountNumber;
    private String bankCode;
    private String customerName;
    private String depositorName;
    private OffsetDateTime dueDate;
    private PaymentRefundStatus refundStatus;
    private boolean expired;
    private PaymentSettlementStatus settlementStatus;
    private String refundReceiveAccountBankCode;
    private String refundReceiveAccountNumber;
    private String refundReceiveAccountHolderName;
}

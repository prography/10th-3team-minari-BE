package com.prography.minari.pg.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class TossPayment {

    // orderId=RjX-D49-287_weWqsbtrzkXArUwCuKuW&paymentKey=tgen_20250810150558v3nD7&amount=100

    @Id
    @Column(length = 64)
    private String paymentKey;

    private String mId;
    private String lastTransactionKey;
    private String orderId;
    private String orderName;

    private BigDecimal taxExemptionAmount;
    private String status;

    private OffsetDateTime requestedAt;
    private OffsetDateTime approvedAt;

    private boolean useEscrow;
    private boolean cultureExpense;

    // Card (중첩)
    private String cardIssuerCode;
    private String cardAcquirerCode;
    private String cardNumber;
    private Integer cardInstallmentPlanMonths;
    private Boolean cardInterestFree;
    private String cardInterestPayer;
    private String cardApproveNo;
    private Boolean cardUseCardPoint;
    private String cardType;
    private String cardOwnerType;
    private String cardAcquireStatus;
    private BigDecimal cardAmount;

    // EasyPay (중첩)
    private String easyPayProvider;
    private BigDecimal easyPayAmount;
    private BigDecimal easyPayDiscountAmount;

    // Receipt
    private String receiptUrl;

    // Checkout
    private String checkoutUrl;

    private String country;
    private Boolean isPartialCancelable;

    private String type;
    private String currency;
    private BigDecimal totalAmount;
    private BigDecimal balanceAmount;
    private BigDecimal suppliedAmount;
    private BigDecimal vat;
    private BigDecimal taxFreeAmount;

    private String method;
    private String version;

}

package com.prography.minari.pg.entity;

import com.prography.minari.pg.dto.common.Payment;
import com.prography.minari.pg.enums.payment.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "toss_payments")
@Getter
@NoArgsConstructor
public class TossPayment {

    private String userId;     //
    private String paymentKey; // 결제의 키값

    private String version;    //

    private PaymentType type;  //
    private String orderId;
    private String orderName;
    private String mId;
    private String currency;
    private PaymentMethod method;
    private BigDecimal totalAmount;
    private BigDecimal balanceAmount;
    private PaymentStatus status;
    private OffsetDateTime requestedAt;
    private OffsetDateTime approvedAt;
    private boolean useEscrow;
    private String lastTransactionKey;
    private BigDecimal suppliedAmount;
    private BigDecimal vat;
    private boolean cultureExpense;
    private BigDecimal taxFreeAmount;
    private BigDecimal taxExemptionAmount;
    private List<TossPaymentCancel> cancels;
    private boolean isPartialCancelable;
    private TossPaymentCard card;
    private TossPaymentVirtualAccount virtualAccount;
    private String secret;

    private String customerMobilePhone;
    private PaymentSettlementStatus mobilePhoneSettlementStatus;
    private String mobilePhoneReceiptUrl;

    private String giftCertificateApproveNo;
    private PaymentSettlementStatus giftCertificateSettlementStatus;

    private String transferBankCode;
    private PaymentSettlementStatus transferSettlementStatus;

    private List<TossPaymentMetadata> metadata;

    private String receiptUrl;

    private String checkoutUrl;

    private String easyPayProvider;
    private BigDecimal easyPayAmount;
    private BigDecimal easyPayDiscountAmount;

    private String country;

    private String failureCode;
    private String failureMessage;

    private TossPaymentCashReceipt cashReceipt;
    private List<TossPaymentCashReceipts> cashReceipts;

    private BigDecimal discountAmount;
}

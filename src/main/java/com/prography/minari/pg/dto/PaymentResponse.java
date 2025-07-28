package com.prography.minari.pg.dto;

import com.prography.minari.pg.entity.TossPayment;

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

    public static TossPayment from(PaymentResponse res) {
        TossPayment e = new TossPayment();

        e.setPaymentKey(res.paymentKey());
        e.setMId(res.mId());
        e.setLastTransactionKey(res.lastTransactionKey());
        e.setOrderId(res.orderId());
        e.setOrderName(res.orderName());
        e.setTaxExemptionAmount(res.taxExemptionAmount());
        e.setStatus(res.status());
        e.setRequestedAt(res.requestedAt());
        e.setApprovedAt(res.approvedAt());
        e.setUseEscrow(res.useEscrow());
        e.setCultureExpense(res.cultureExpense());
        e.setIsPartialCancelable(res.isPartialCancelable());
        e.setType(res.type());
        e.setCurrency(res.currency());
        e.setTotalAmount(res.totalAmount());
        e.setBalanceAmount(res.balanceAmount());
        e.setSuppliedAmount(res.suppliedAmount());
        e.setVat(res.vat());
        e.setTaxFreeAmount(res.taxFreeAmount());
        e.setMethod(res.method());
        e.setVersion(res.version());
        e.setCountry(res.country());

        if (res.card() != null) {
            e.setCardIssuerCode(res.card().issuerCode());
            e.setCardAcquirerCode(res.card().acquirerCode());
            e.setCardNumber(res.card().number());
            e.setCardInstallmentPlanMonths(res.card().installmentPlanMonths());
            e.setCardInterestFree(res.card().isInterestFree());
            e.setCardInterestPayer(res.card().interestPayer());
            e.setCardApproveNo(res.card().approveNo());
            e.setCardUseCardPoint(res.card().useCardPoint());
            e.setCardType(res.card().cardType());
            e.setCardOwnerType(res.card().ownerType());
            e.setCardAcquireStatus(res.card().acquireStatus());
            e.setCardAmount(res.card().amount());
        }

        if (res.easyPay() != null) {
            e.setEasyPayProvider(res.easyPay().provider());
            e.setEasyPayAmount(res.easyPay().amount());
            e.setEasyPayDiscountAmount(res.easyPay().discountAmount());
        }

        if (res.receipt() != null) {
            e.setReceiptUrl(res.receipt().url());
        }

        if (res.checkout() != null) {
            e.setCheckoutUrl(res.checkout().url());
        }

        return e;
    }

}


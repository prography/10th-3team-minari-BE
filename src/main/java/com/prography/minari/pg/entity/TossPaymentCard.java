package com.prography.minari.pg.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Table(name = "toss_payment_cards")
@NoArgsConstructor
public class TossPaymentCard {
    @Id
    String paymentKey;             //
    String issuerCode;
    String acquirerCode;
    String number;
    String interestPayer;
    String approveNo;
    String cardType;
    String ownerType;
    String acquireStatus;
    Integer installmentPlanMonths; //
    BigDecimal amount;
    boolean isInterestFree;
    boolean useCardPoint;
}

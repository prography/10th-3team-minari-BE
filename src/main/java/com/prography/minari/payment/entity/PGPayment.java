package com.prography.minari.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "toss_payment")
public class PGPayment extends Payment {

    @Column(unique = true, nullable = false, updatable = false, length = 200)
    private String paymentKey;

    protected PGPayment(Long userId, Long productId, Long amount) {
        super(userId, productId, amount);
    }

    public static PGPayment create(Long userId, Long productId, Long amount, String paymentKey) {
        PGPayment pgPayment = new PGPayment(userId, productId, amount);
        pgPayment.paymentKey = paymentKey;
        return pgPayment;
    }

}

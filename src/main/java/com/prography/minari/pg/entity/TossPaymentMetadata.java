package com.prography.minari.pg.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "toss_payment_metadata")
public class TossPaymentMetadata {

    @Id
    private String paymentKey;
    private Integer seq;
    private String key;
    private String value;
}

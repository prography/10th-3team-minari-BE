package com.prography.minari.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCTS")
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "real_price")
    private Long realPrice;

    @Column(name = "fake_price")
    private Long fakePrice;

    @Column(name = "message")
    private String message;

    @Column(name = "discount_rate")
    private int discountRate;

    // 상품 할인 종료, 또는 이벤트 종료 시각
    @Column(name = "discount_end_date")
    private LocalDate discountEndDate;

    // 상품 할인 시작, 또는 이벤트 시작 시각
    @Column(name = "discount_start_date")
    private LocalDate discountStartDate;

    @Column(name = "active")
    private boolean active;

    @Column(name = "pay_category")
    @Enumerated(EnumType.STRING)
    private PayCategory payCategory;

    @Column(name = "expiration_type")
    @Enumerated(EnumType.STRING)
    private ExpirationType expirationType;

    @Column(name = "expiration_datetime")
    private LocalDateTime expirationDateTime;

    @Column(name = "expiration_period_value")
    private Integer expirationPeriodValue;
}

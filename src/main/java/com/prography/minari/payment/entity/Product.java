package com.prography.minari.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

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
    @Column(name = "discount_end_date")
    private LocalDate discountEndDate;
    @Column(name = "discount_start_date")
    private LocalDate discountStartDate;
    @Column(name = "active")
    private boolean active;
    @Column(name = "pay_category")
    @Enumerated(EnumType.STRING)
    private PayCategory payCategory;
}

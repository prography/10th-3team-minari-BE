package com.prography.minari.payment.dto;

import com.prography.minari.payment.entity.Credit;
import com.prography.minari.payment.entity.Product;

public record CreditProductDto(Credit credit, Product product) {}

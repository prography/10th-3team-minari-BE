package com.prography.minari.payment.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.payment.entity.Product;
import com.prography.minari.payment.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@ImplService
@RequiredArgsConstructor
public class ProductReader {
    private final ProductJpaRepository productJpaRepository;

    public List<Product> readAllActive() {
        return productJpaRepository.findAllActive();
    }

    public Optional<Product> read(Long productId) {
        return productJpaRepository.findById(productId);
    }
}

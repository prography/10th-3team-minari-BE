package com.prography.minari.payment.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.payment.entity.Credit;
import com.prography.minari.payment.repository.CreditJpaRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@ImplService
@RequiredArgsConstructor
public class CreditReader {
    private final CreditJpaRepository creditJpaRepository;

    public Optional<Credit> readGetTodayByProductId(Long productId,Long userId) {
        return creditJpaRepository.findByProductIdAndCreatedAtToday(
                productId,
                userId,
                LocalDateTime.now().toLocalDate().atStartOfDay(),
                LocalDateTime.now().plusDays(1L).toLocalDate().atStartOfDay());
    }
}

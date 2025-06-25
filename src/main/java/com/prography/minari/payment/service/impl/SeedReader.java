package com.prography.minari.payment.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.payment.entity.Seed;
import com.prography.minari.payment.repository.SeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ImplService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeedReader {
    private final SeedRepository seedRepository;

    public Optional<Seed> readByUserId(Long userId) {
        return seedRepository.findByUserId(userId);
    }
}

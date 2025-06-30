package com.prography.minari.payment.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.payment.entity.Seed;
import com.prography.minari.payment.repository.SeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ImplService
@RequiredArgsConstructor
@Transactional
public class SeedWriter {
    private final SeedRepository seedRepository;

    public Seed write(Seed seed) {
        return seedRepository.save(seed);
    }
}

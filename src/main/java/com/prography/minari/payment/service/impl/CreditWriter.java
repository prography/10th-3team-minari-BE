package com.prography.minari.payment.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.payment.entity.Credit;
import com.prography.minari.payment.repository.CreditJpaRepository;
import lombok.RequiredArgsConstructor;

@ImplService
@RequiredArgsConstructor
public class CreditWriter {
    private final CreditJpaRepository creditJpaRepository;
    public Credit write(Credit credit){
        return creditJpaRepository.save(credit);
    }
}

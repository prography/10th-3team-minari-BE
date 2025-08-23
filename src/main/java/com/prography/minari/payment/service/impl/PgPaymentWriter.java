package com.prography.minari.payment.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.payment.entity.PGPayment;
import com.prography.minari.payment.repository.PgPaymentRepository;
import lombok.RequiredArgsConstructor;

@ImplService
@RequiredArgsConstructor
public class PgPaymentWriter {

    private final PgPaymentRepository pgPaymentRepository;

    public PGPayment write(PGPayment pgPayment) {
        return pgPaymentRepository.save(pgPayment);
    }

}

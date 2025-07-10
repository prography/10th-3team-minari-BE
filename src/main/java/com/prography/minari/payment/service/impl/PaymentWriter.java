package com.prography.minari.payment.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.payment.entity.Payment;
import com.prography.minari.payment.entity.PaymentLog;
import com.prography.minari.payment.repository.CreditJpaRepository;
import com.prography.minari.payment.repository.CreditUsageJpaRepository;
import com.prography.minari.payment.repository.PaymentJpaRepository;
import com.prography.minari.payment.repository.PaymentLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@ImplService
@RequiredArgsConstructor
@Transactional
public class PaymentWriter {
    private final PaymentLogRepository paymentLogRepository;
    private final PaymentJpaRepository paymentJpaRepository;
    public PaymentLog write(PaymentLog paymentLog) {
        return paymentLogRepository.save(paymentLog);
    }

    public Payment write(Payment payment) {
        return paymentJpaRepository.save(payment);
    }
}
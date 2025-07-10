package com.prography.minari.payment.service;

import com.prography.minari.payment.service.impl.CreditCounter;
import com.prography.minari.user.service.impl.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final CreditCounter creditCounter;

    public Long leftCredits(Long userId){
        return creditCounter.countNotUsedCredit(userId);
    }
}

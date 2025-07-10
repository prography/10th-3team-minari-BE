package com.prography.minari.payment.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.payment.entity.Credit;
import com.prography.minari.payment.entity.CreditUsage;
import com.prography.minari.payment.repository.CreditJpaRepository;
import com.prography.minari.payment.repository.CreditUsageJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ImplService
@RequiredArgsConstructor
public class CreditCounter {
    private final CreditJpaRepository creditJpaRepository;
    private final CreditUsageJpaRepository creditUsageJpaRepository;

    public Long countNotUsedCredit(Long userId){
        // 환불도 고려해야함.
        // 현재 씨앗의 수 = 전체 결제된 씨앗의 수 - 환불된 씨앗 - (환불안된 credit - 사용한 씨앗의 수)
        List<Credit> creditsOfUser = creditJpaRepository.findAllByUserId(userId);

        long totalCreditAmount = creditsOfUser.stream()
                .mapToLong(Credit::getAmount)
                .sum();

        long refundedCreditAmount = creditsOfUser.stream()
                .filter(Credit::isRefund)
                .mapToLong(Credit::getAmount)
                .sum();

        List<Long> usingCreditIds = creditsOfUser.stream()
                .filter(credit -> !credit.isRefund())
                .map(Credit::getId)
                .toList();

        List<CreditUsage> usagesOfNotRefund = creditUsageJpaRepository.findAllByCreditIdIn(usingCreditIds);
        long totalAmountOfUserUsage = usagesOfNotRefund.stream()
                .mapToLong(CreditUsage::getUsedAmount)
                .sum();

        return totalCreditAmount - refundedCreditAmount -  totalAmountOfUserUsage;
    }
}

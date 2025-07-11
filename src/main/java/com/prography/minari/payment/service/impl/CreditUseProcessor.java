package com.prography.minari.payment.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.common.entity.BaseTimeEntity;
import com.prography.minari.payment.entity.Credit;
import com.prography.minari.payment.entity.CreditUsage;
import com.prography.minari.payment.repository.CreditJpaRepository;
import com.prography.minari.payment.repository.CreditUsageJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static com.prography.minari.payment.service.impl.CreditUsageTarget.INTERVIEW;

@ImplService
@RequiredArgsConstructor
public class CreditUseProcessor {
    private final CreditJpaRepository creditJpaRepository;
    private final CreditUsageJpaRepository creditUsageJpaRepository;

    public void use(Long userId, Long used, CreditUsageTarget target) {
        List<Credit> credits = creditJpaRepository.findAllByUserId(userId).stream()
                .sorted(Comparator.comparing(BaseTimeEntity::getCreatedDateTime, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();

        List<Long> creditIds = credits.stream()
                .map(Credit::getId)
                .toList();

        List<CreditUsage> creditUsages = creditUsageJpaRepository.findAllByCreditIdIn(creditIds);

        Map<Long, Long> usages = new HashMap<>();
        for (CreditUsage creditUsage : creditUsages) {
            Long creditId = creditUsage.getCredit().getId();
            usages.put(creditId, usages.getOrDefault(creditId, 0L) + creditUsage.getUsedAmount());
        }

        List<CreditUsage> usedList = new ArrayList<>();
        for (Credit credit : credits) {
            if (used <= 0) break;

            Long initAmount = credit.getAmount();
            Long usedCreditAmount = usages.getOrDefault(credit.getId(), 0L);
            Long remaining = initAmount - usedCreditAmount;

            if (remaining <= 0) continue;

            if (remaining >= used) {
                usedList.add(CreditUsage.builder()
                        .credit(credit)
                        .usedTarget(target)
                        .usedAmount(used)
                        .build());
                used = 0L;
            } else {
                usedList.add(CreditUsage.builder()
                        .credit(credit)
                        .usedTarget(target)
                        .usedAmount(remaining)
                        .build());
                used -= remaining;
            }
        }

        if (used > 0) {
            throw new IllegalStateException("사용 가능한 크레딧이 부족합니다.");
        }

        creditUsageJpaRepository.saveAll(usedList);
    }


    public Map<Credit, Long> getHistory(Long userId) {
        List<Credit> credits = creditJpaRepository.findAllByUserId(userId);

        List<Long> creditIds = credits.stream()
                .map(Credit::getId)
                .toList();

        List<CreditUsage> creditUsages = creditUsageJpaRepository.findAllByCreditIdIn(creditIds);

        Map<Long, Long> usages = new HashMap<>();

        for (CreditUsage creditUsage : creditUsages) {
            Long creditId = creditUsage.getCredit().getId();
            usages.put(creditId, usages.getOrDefault(creditId, 0L) + creditUsage.getUsedAmount());
        }

        Map<Credit, Long> creditMap = new HashMap<>();
        for (Credit credit : credits) {
            creditMap.putIfAbsent(credit, 0L);
            creditMap.put(credit, usages.getOrDefault(credit.getId(), 0L));
        }

        return creditMap;
    }
}

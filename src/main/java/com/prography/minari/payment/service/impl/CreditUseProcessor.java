package com.prography.minari.payment.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.common.entity.BaseTimeEntity;
import com.prography.minari.payment.dto.CreditProductDto;
import com.prography.minari.payment.entity.Credit;
import com.prography.minari.payment.entity.CreditUsage;
import com.prography.minari.payment.entity.PayCategory;
import com.prography.minari.payment.repository.CreditJpaRepository;
import com.prography.minari.payment.repository.CreditUsageJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.*;

import static com.prography.minari.payment.entity.CreditStatus.PAID;
import static com.prography.minari.payment.service.impl.CreditUsageTarget.INTERVIEW;

@ImplService
@RequiredArgsConstructor
public class CreditUseProcessor {
    private final CreditJpaRepository creditJpaRepository;
    private final CreditUsageJpaRepository creditUsageJpaRepository;

    public void use(Long userId, Long used, CreditUsageTarget target) {

        List<CreditProductDto> creditProductDtos = creditJpaRepository.findAllByUserId(userId).stream()
                .filter(c -> c.credit().getStatus().equals(PAID))
                .sorted(
                        Comparator.comparing(
                                CreditProductDto::product,  // product() 기준
                                Comparator.nullsLast(
                                        Comparator.comparing(p ->
                                                p.getPayCategory().equals(PayCategory.EVENT) ? 0 : 1
                                        )
                                )
                        ).thenComparing(dto -> dto.credit().getCreatedDateTime())
                )
                .toList();

        List<Long> creditIds = creditProductDtos.stream()
                .map(cp->cp.credit().getId())
                .toList();

        List<CreditUsage> creditUsages = creditUsageJpaRepository.findAllByCreditIdIn(creditIds);

        Map<Long, Long> usages = new HashMap<>();
        for (CreditUsage creditUsage : creditUsages) {
            Long creditId = creditUsage.getCredit().getId();
            usages.put(creditId, usages.getOrDefault(creditId, 0L) + creditUsage.getUsedAmount());
        }

        List<CreditUsage> usedList = new ArrayList<>();
        List<Credit> credits = creditProductDtos.stream()
                .map(CreditProductDto::credit)
                .toList();
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
        List<Credit> credits = creditJpaRepository.findAllByUserId(userId).stream()
                .map(c -> c.credit())
                .sorted(Comparator.comparing(Credit::getCreatedDateTime))
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

        Map<Credit, Long> creditMap = new HashMap<>();
        for (Credit credit : credits) {
            creditMap.putIfAbsent(credit, 0L);
            creditMap.put(credit, usages.getOrDefault(credit.getId(), 0L));
        }

        return creditMap;
    }
}

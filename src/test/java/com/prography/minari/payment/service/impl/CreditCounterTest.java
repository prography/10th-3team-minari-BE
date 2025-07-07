package com.prography.minari.payment.service.impl;

import com.prography.minari.payment.entity.Credit;
import com.prography.minari.payment.entity.CreditUsage;
import com.prography.minari.payment.repository.CreditJpaRepository;
import com.prography.minari.payment.repository.CreditUsageJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditCounterTest {
    @Mock
    private CreditJpaRepository creditJpaRepository;

    @Mock
    private CreditUsageJpaRepository creditUsageJpaRepository;

    @InjectMocks
    private CreditCounter creditService;
    @Test
    void 현재_보유하고있는_씨앗의_개수조회() {
        Long userId = 1L;

        // 3개의 크레딧: 2개는 환불되지 않았고, 1개는 환불됨
        Credit credit1 = mock(Credit.class);
        when(credit1.getId()).thenReturn(1L);
        when(credit1.getAmount()).thenReturn(100L);
        when(credit1.isRefund()).thenReturn(false);

        Credit credit2 = mock(Credit.class);
        when(credit2.getAmount()).thenReturn(50L);
        when(credit2.isRefund()).thenReturn(true); // 환불됨

        Credit credit3 = mock(Credit.class);
        when(credit3.getId()).thenReturn(3L);
        when(credit3.getAmount()).thenReturn(200L);
        when(credit3.isRefund()).thenReturn(false);

        List<Credit> creditsOfUser = List.of(credit1, credit2, credit3);
        when(creditJpaRepository.findAllByUserId(userId)).thenReturn(creditsOfUser);

        // 사용 내역: credit1에서 30, credit3에서 50 사용
        CreditUsage usage1 = mock(CreditUsage.class);
        when(usage1.getUsedAmount()).thenReturn(30L);
        CreditUsage usage2 = mock(CreditUsage.class);
        when(usage2.getUsedAmount()).thenReturn(50L);

        List<CreditUsage> usages = List.of(usage1, usage2);
        when(creditUsageJpaRepository.findAllByCreditIdIn(List.of(1L, 3L))).thenReturn(usages);

        // 실행
        Long result = creditService.countNotUsedCredit(userId);

        /*
         totalCreditAmount: 100 + 50 + 200 = 350
         refundedCreditAmount: 50
         totalAmountOfNotRefund: 100 + 200 = 300
         totalAmountOfUserUsage: 30 + 50 = 80

         return = 350 - 50 - (300 - 80) = 80
         */
        assertThat(80L).isEqualTo(result);
    }

}
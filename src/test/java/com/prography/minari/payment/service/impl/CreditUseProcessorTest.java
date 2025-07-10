package com.prography.minari.payment.service.impl;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.prography.minari.payment.entity.Credit;
import com.prography.minari.payment.entity.CreditUsage;
import com.prography.minari.payment.repository.CreditJpaRepository;
import com.prography.minari.payment.repository.CreditUsageJpaRepository;
import com.prography.minari.payment.service.CreditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static com.prography.minari.payment.service.impl.CreditUsageTarget.INTERVIEW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditUseProcessorTest {
    @InjectMocks
    private CreditUseProcessor creditUseProcessor;

    @Mock
    private CreditJpaRepository creditJpaRepository;

    @Mock
    private CreditUsageJpaRepository creditUsageJpaRepository;

    private FixtureMonkey fixtureMonkey;

    @BeforeEach
    void setUp() {
        fixtureMonkey = FixtureMonkey.builder()
                .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                .build();
    }

    @Test
    void 크레딧을_순차적으로_사용() {
        // given
        Long userId = 1L;
        Long totalUsedAmount = 180L;

        // Credit 3개 생성 (100, 50, 200)
        Credit credit1 = fixtureMonkey.giveMeBuilder(Credit.class)
                .set("id", 1L)
                .set("userId", userId)
                .set("amount", 100L)
                .set("createdDateTime", LocalDateTime.now().minusDays(2))
                .sample();

        Credit credit2 = fixtureMonkey.giveMeBuilder(Credit.class)
                .set("id", 2L)
                .set("userId", userId)
                .set("amount", 50L)
                .set("createdDateTime", LocalDateTime.now().minusDays(1))
                .sample();

        Credit credit3 = fixtureMonkey.giveMeBuilder(Credit.class)
                .set("id", 3L)
                .set("userId", userId)
                .set("amount", 200L)
                .set("createdDateTime", LocalDateTime.now())
                .sample();

        List<Credit> credits = List.of(credit1, credit2, credit3);

        when(creditJpaRepository.findAllByUserId(userId)).thenReturn(credits);
        when(creditUsageJpaRepository.findAllByCreditIdIn(List.of(1L, 2L, 3L)))
                .thenReturn(List.of());

        ArgumentCaptor<List<CreditUsage>> captor = ArgumentCaptor.forClass(List.class);

        // when
        creditUseProcessor.use(userId, totalUsedAmount, INTERVIEW);

        // then
        verify(creditUsageJpaRepository).saveAll(captor.capture());
        List<CreditUsage> result = captor.getValue();

        assertAll(
                () -> assertThat(result).hasSize(3),
                () -> assertThat(result.get(0).getCredit().getId()).isEqualTo(1L),
                () -> assertThat(result.get(0).getUsedAmount()).isEqualTo(100L),

                () -> assertThat(result.get(1).getCredit().getId()).isEqualTo(2L),
                () -> assertThat(result.get(1).getUsedAmount()).isEqualTo(50L),

                () -> assertThat(result.get(2).getCredit().getId()).isEqualTo(3L),
                () -> assertThat(result.get(2).getUsedAmount()).isEqualTo(30L)
        );


    }
}
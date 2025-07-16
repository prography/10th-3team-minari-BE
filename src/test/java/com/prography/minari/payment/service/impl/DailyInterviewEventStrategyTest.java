package com.prography.minari.payment.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.prography.minari.payment.entity.*;
import com.prography.minari.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DailyInterviewEventStrategyTest {

    @Mock
    private ProductReader productReader;

    @Mock
    private PaymentWriter paymentWriter;

    @Mock
    private CreditWriter creditWriter;

    @InjectMocks
    private DailyInterviewEventStrategy strategy;
    private FixtureMonkey fixtureMonkey;
    private ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    void setUp() {
        fixtureMonkey = FixtureMonkey.builder()
                .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                .build();
    }

    @Test
    void 사용자_데일리_최초면접진행_이벤트_크레딧발급테스트() throws Exception {
        // given
        User user = fixtureMonkey.giveMeBuilder(User.class)
                .set("id", 1L)
                .sample();

        Product product = fixtureMonkey.giveMeBuilder(Product.class)
                .set("id", 100L)
                .set("quantity", 5)
                .sample();

        Long productId = product.getId();
        String metadataJson = objectMapper.writeValueAsString(Map.of("productId", productId));
        EventMeta eventMeta = fixtureMonkey.giveMeBuilder(EventMeta.class)
                .set("metadata", metadataJson)
                .sample();

        EventContext context = new EventContext(user, eventMeta);

        when(productReader.read(productId)).thenReturn(Optional.of(product));

        ArgumentCaptor<AccountPayment> paymentCaptor = ArgumentCaptor.forClass(AccountPayment.class);
        ArgumentCaptor<Credit> creditCaptor = ArgumentCaptor.forClass(Credit.class);

        // when
        strategy.execute(context);

        // then
        verify(productReader).read(productId);
        verify(paymentWriter).write(paymentCaptor.capture());
        verify(creditWriter).write(creditCaptor.capture());

        AccountPayment writtenPayment = paymentCaptor.getValue();
        assertEquals(user.getId(), writtenPayment.getUserId());
        assertEquals(productId, writtenPayment.getProductId());
        assertEquals(Long.valueOf(product.getQuantity()), writtenPayment.getAmount());

        Credit writtenCredit = creditCaptor.getValue();
        assertEquals(user.getId(), writtenCredit.getUserId());
        assertEquals(productId, writtenCredit.getProductId());
        assertEquals(Long.valueOf(product.getQuantity()), writtenCredit.getAmount());
        assertEquals(CreditStatus.PAID, writtenCredit.getStatus());
    }
}
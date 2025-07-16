package com.prography.minari.payment.service.impl;

import com.prography.minari.payment.entity.AccountPayment;
import com.prography.minari.payment.entity.Credit;
import com.prography.minari.payment.entity.CreditStatus;
import com.prography.minari.payment.entity.Product;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyInterviewEventStrategy implements EventStrategy {
    private final CreditWriter creditWriter;
    private final PaymentWriter paymentWriter;
    private final ProductReader productReader;

    @Override
    public EventTrigger getSupportedTrigger() {
        return EventTrigger.DAILY_INTERVIEW_EVENT;
    }

    @Override
    public String getEventName() {
        return "NEW_USER_GIFT";
    }

    @Override
    public void execute(EventContext context) {
        Long productId = Long.valueOf(context.getMetadata().get("productId").toString());
        User user = context.getUser();
        Optional<Product> productOpt = productReader.read(productId);
        productOpt.ifPresentOrElse(product -> {
            AccountPayment accountPayment = AccountPayment.create(user.getId(),
                    productId,
                    (long) product.getQuantity(),
                    null,
                    "사용자 데일리 면접진행 이벤트",
                    null);
            paymentWriter.write(accountPayment);
            creditWriter.write(Credit.builder()
                    .paymentId(accountPayment.getId())
                    .userId(user.getId())
                    .amount((long) product.getQuantity())
                    .productId(productId)
                    .status(CreditStatus.PAID)
                    .expiredDateTime(LocalDateTime.now().toLocalDate().plusDays(1L).atStartOfDay())
                    .build());
        }, () -> log.info("product not found: {}", productId));
    }
}

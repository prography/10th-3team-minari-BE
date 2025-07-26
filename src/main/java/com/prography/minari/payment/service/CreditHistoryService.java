package com.prography.minari.payment.service;

import com.prography.minari.payment.entity.Credit;
import com.prography.minari.payment.entity.CreditStatus;
import com.prography.minari.payment.entity.PayCategory;
import com.prography.minari.payment.entity.Product;
import com.prography.minari.payment.service.dto.CreditUsageHistoryResponseDto;
import com.prography.minari.payment.service.impl.CreditUseProcessor;
import com.prography.minari.payment.service.impl.ProductReader;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.impl.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.prography.minari.payment.entity.CreditStatus.EXPIRED;
import static com.prography.minari.payment.entity.CreditStatus.REFUND;

@Service
@RequiredArgsConstructor
public class CreditHistoryService {
    private final CreditUseProcessor creditUseProcessor;
    private final ProductReader productReader;

    public List<CreditUsageHistoryResponseDto> getCreditUsageHistory(Long userId) {
        Map<Credit, Long> history = creditUseProcessor.getHistory(userId);
        List<CreditUsageHistoryResponseDto> results = new ArrayList<>();
        for (Map.Entry<Credit, Long> entry : history.entrySet()) {
            Credit credit = entry.getKey();
            String category = "BUY";
//            PayCategory payCategory = PayCategory.BUY;

            Optional<Product> productOpt = productReader.read(credit.getProductId());
            if (credit.getStatus().equals(EXPIRED)) {
                category = EXPIRED.toString();
            } else if (productOpt.isPresent()) {
                Product product = productOpt.get();
                category = product.getPayCategory().toString();
            }

            Long amount = entry.getValue();
            CreditStatus status = credit.getStatus();

            CreditUsageHistoryResponseDto dto = CreditUsageHistoryResponseDto.builder()
                    .refund(status.equals(CreditStatus.REFUND))
                    .date(credit.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")))
                    .category(category)
                    .quantity(credit.getAmount())
                    .remain(credit.getAmount() - amount)
                    .build();

            results.add(dto);
        }
        return results.stream()
                .sorted(Comparator.comparing(CreditUsageHistoryResponseDto::getDate).reversed())
                .toList();
    }
}

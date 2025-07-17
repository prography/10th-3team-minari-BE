package com.prography.minari.batch.item.processor;

import com.prography.minari.payment.entity.Credit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpiredCreditStatusChangeProcessor {

    public ItemProcessor<Credit, Credit> processor() {
        return credit -> {
            log.info("Expired credit status: {}", credit.getStatus());
            credit.changeStatusOfExpiredCredit();
            return credit;
        };
    }
}

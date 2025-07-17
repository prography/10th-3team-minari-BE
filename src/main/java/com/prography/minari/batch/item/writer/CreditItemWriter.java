package com.prography.minari.batch.item.writer;

import com.prography.minari.payment.entity.Credit;
import com.prography.minari.payment.repository.CreditJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreditItemWriter{
    private final CreditJpaRepository creditRepository;

    public ItemWriter<Credit> write(){
        return creditRepository::saveAll;
    }
}

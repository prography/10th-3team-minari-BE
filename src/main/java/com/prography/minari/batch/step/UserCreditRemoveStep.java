package com.prography.minari.batch.step;

import com.prography.minari.batch.item.processor.ExpiredCreditStatusChangeProcessor;
import com.prography.minari.batch.item.reader.CreditItemReader;
import com.prography.minari.batch.item.writer.CreditItemWriter;
import com.prography.minari.payment.entity.Credit;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class UserCreditRemoveStep {
    private final CreditItemReader creditItemReader;
    private final CreditItemWriter creditItemWriter;
    private final ExpiredCreditStatusChangeProcessor expiredCreditStatusChangeProcessor;

    public Step changeStatusOfExpiredCredit(JobRepository jobRepository,
                               PlatformTransactionManager transactionManager,
                                            DataSource dataSource) throws Exception {
        return new StepBuilder("changeStatusOfExpiredCredit", jobRepository)
                .<Credit, Credit>chunk(100, transactionManager)
                .reader(creditItemReader.readExpiredCredit(dataSource))
                .processor(expiredCreditStatusChangeProcessor.processor())
                .writer(creditItemWriter.write())
                .build();
    }
}

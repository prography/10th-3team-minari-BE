package com.prography.minari.batch.job;

import com.prography.minari.batch.step.UserCreditRemoveStep;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Component
@RequiredArgsConstructor
public class CreditJobConfiguration {
    private final UserCreditRemoveStep userCreditRemoveStep;
    @Bean
    public Job remove(JobRepository jobRepository, PlatformTransactionManager transactionManager, DataSource dataSource) throws Exception {
        return new JobBuilder("remove-expired-credit", jobRepository)
                .start(userCreditRemoveStep.changeStatusOfExpiredCredit(jobRepository, transactionManager,dataSource))
                .build();
    }
}

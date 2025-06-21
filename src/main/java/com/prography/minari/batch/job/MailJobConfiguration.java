package com.prography.minari.batch.job;

import com.prography.minari.batch.step.MailSendStep;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class MailJobConfiguration {
    private final MailSendStep mailSendStep;

    @Bean
    public Job start(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new JobBuilder("mail-send-batch",jobRepository)
                .start(mailSendStep.sendMailToUser(jobRepository, transactionManager))
                .build();
    }

}

package com.prography.minari.batch.jobLauncher;

import com.prography.minari.batch.job.MailJobConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MailSendLauncher {
    private final JobLauncher jobLauncher;
    private final MailJobConfiguration mailJobConfiguration;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Scheduled(cron = "0 0 8 * * *")
    public void launch() throws Exception {
        JobParameters mailSendDateParameter = new JobParametersBuilder()
                .addLocalDateTime("sendMailDate", LocalDateTime.now())
                .toJobParameters();
        jobLauncher.run(mailJobConfiguration.start(jobRepository, transactionManager), mailSendDateParameter);
        ;
    }
}

package com.prography.minari.batch.jobLauncher;

import com.prography.minari.batch.job.MailJobConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailSendLauncher {
    private final JobLauncher jobLauncher;
    private final MailJobConfiguration mailJobConfiguration;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Scheduled(cron = "0 0 8 * * *")
    public void launch() throws Exception {
        log.info("[{}] Starting mail send job",LocalDateTime.now());
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLocalDate("sendMailDate", LocalDate.now())
                    .toJobParameters();

            jobLauncher.run(mailJobConfiguration.start(jobRepository, transactionManager), params);
        } catch (JobInstanceAlreadyCompleteException e) {
            log.info("Job already completed: {}", e.getMessage());
        } catch (JobExecutionAlreadyRunningException |
                 JobRestartException |
                 JobParametersInvalidException e) {
            log.error("Job failed to start", e);
        }finally {
            log.info("[{}] Finished mail send job",LocalDateTime.now());
        }
    }
}

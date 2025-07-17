package com.prography.minari.batch.jobLauncher;

import com.prography.minari.batch.job.CreditJobConfiguration;
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

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreditRemoveLauncher {
    private final JobLauncher jobLauncher;
    private final CreditJobConfiguration creditJobConfiguration;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;

    @Scheduled(cron = "0 0 0 * * *")
    public void launch() throws Exception {
        log.info("[{}] Starting change expired credit status job", LocalDateTime.now());
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLocalDate("changeStatusOfExpiredCredit", LocalDate.now())
                    .toJobParameters();

            jobLauncher.run(creditJobConfiguration.remove(jobRepository, transactionManager,dataSource), params);
            log.info("[{}] Finished change expired credit status job", LocalDateTime.now());
        } catch (JobInstanceAlreadyCompleteException e) {
            log.info("expired credit Job already completed: {}", e.getMessage());
        } catch (JobExecutionAlreadyRunningException |
                 JobRestartException |
                 JobParametersInvalidException e) {
            log.error("expired credit Job failed to start", e);
        }
    }
}

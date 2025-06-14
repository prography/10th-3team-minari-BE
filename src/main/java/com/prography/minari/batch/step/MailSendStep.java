package com.prography.minari.batch.step;

import com.prography.minari.batch.item.processor.MailContentCreateProcessor;
import com.prography.minari.batch.item.reader.UserItemReader;
import com.prography.minari.batch.item.writer.MailSendWriter;
import com.prography.minari.mail.dto.MailRequest;
import com.prography.minari.user.entity.User;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

@Component
@RequiredArgsConstructor
public class MailSendStep {
    private final UserItemReader userItemReader;
    private final EntityManagerFactory emf;
    private final MailContentCreateProcessor mailContentCreateProcessor;
    private final MailSendWriter mailSendWriter;

    public Step sendMailToUser(JobRepository jobRepository,
                               PlatformTransactionManager transactionManager) {
        return new StepBuilder("sendDailyQuestionToUser", jobRepository)
                .<User, MailRequest>chunk(100, transactionManager)
                .reader(userItemReader.reader(emf))
                .processor(mailContentCreateProcessor.processor())
                .writer(mailSendWriter.send())
                .build();
    }
}

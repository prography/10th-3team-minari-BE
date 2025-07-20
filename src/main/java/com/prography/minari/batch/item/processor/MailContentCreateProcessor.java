package com.prography.minari.batch.item.processor;

import com.prography.minari.common.entity.Domain;
import com.prography.minari.mail.dto.MailRequest;
import com.prography.minari.mail.service.impl.MailTemplateCreater;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.service.impl.QuestionReader;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MailContentCreateProcessor {
    private final MailTemplateCreater mailTemplateCreater;
    private final QuestionReader questionReader;

    public ItemProcessor<User, MailRequest> processor() {
        return user -> {

            Optional<Question> questionOpt = questionReader.readDaily(
                    user,
                    user.getPreferDomains(),
                    user.getDaysSinceJoined()
            );

            return questionOpt.map(question ->
                    mailTemplateCreater.create(
                            String.format("%d월 %d일 오늘의 미나리가 도착했어요!",
                                    LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()),
                            user.getEmail(),
                            Map.of("category", question.getDomain().toString(), "keyword", question.getTags().getFirst()),
                            "today-minari")
            ).orElse(
                    mailTemplateCreater.create(
                            "오늘의 미나리",
                            user.getEmail(),
                            Map.of(),
                            "no-have-problem.html")
            );
        };
    }
}

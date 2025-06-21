package com.prography.minari.batch.item.processor;

import com.prography.minari.common.entity.Domain;
import com.prography.minari.mail.dto.MailRequest;
import com.prography.minari.mail.service.impl.MailTemplateCreater;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.service.impl.QuestionReader;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MailContentCreateProcessor {
    private final MailTemplateCreater mailTemplateCreater;
    private final QuestionReader questionReader;
    @Bean
    public ItemProcessor<User, MailRequest> processor() {
        return user -> {
            Optional<Question> questionOpt = questionReader.readDaily(user, List.of(Domain.CS, user.getDomain()), user.getDaysSinceJoined());
            if(questionOpt.isPresent()) {
                Question question = questionOpt.get();
                return mailTemplateCreater.create("오늘의 미나리",
                        user.getEmail(),
                        Map.of("category",question.getDomain().toString(),"keyword",question.getTags().getFirst()),
                        "today-minari");
            }
            return mailTemplateCreater.create("오늘의 미나리",
                    user.getEmail(),
                    Map.of(),
                    "no-have-problem.html");
        };
    }
}

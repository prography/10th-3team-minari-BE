package com.prography.minari.question.service;

import com.prography.minari.common.entity.Domain;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.question.DailyUserQuestionResDto;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.service.impl.QuestionReader;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.impl.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionReader questionReader;
    private final UserReader userReader;

    public String readContents(Long id) {
        Question question = questionReader.read(id).orElseThrow(() -> new ApiException(ErrorCode.QUESTION_NOT_FOUND));
        return question.getContent();
    }

    public DailyUserQuestionResDto readDaily(Long userId) {
        User user = userReader.read(userId);
        Long daysBetween = user.getDaysSinceJoined();
        List<Domain> domains = user.getPreferDomains();
        Optional<Question> question = questionReader.readDaily(user, domains, daysBetween);
        return question
                .map(q -> new DailyUserQuestionResDto(q.getId(), q.getContent()))
                .orElseThrow(() -> new ApiException(ErrorCode.QUESTION_NOT_FOUND));
    }
}

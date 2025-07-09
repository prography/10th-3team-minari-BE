package com.prography.minari.answer.service;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.prography.minari.answer.entity.Answer;
import com.prography.minari.answer.service.dto.InterviewAccessStatus;
import com.prography.minari.answer.service.impl.AnswerReader;
import com.prography.minari.common.entity.Domain;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.payment.service.impl.CreditCounter;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.service.impl.QuestionReader;
import com.prography.minari.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AnswerServiceUnitTest {
    @InjectMocks
    private AnswerService answerService;

    @Mock
    private QuestionReader questionReader;

    @Mock
    private AnswerReader answerReader;

    @Mock
    private CreditCounter creditCounter;

    private FixtureMonkey fixtureMonkey;

    @BeforeEach
    void setUp() {
        fixtureMonkey = FixtureMonkey.builder()
                .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                .build();
    }

    @RepeatedTest(5)
    void 최초_면접_응시_가능한_상태면_FIRST를_반환한다() {
        // given
        User user = fixtureMonkey.giveMeBuilder(User.class)
                .setNotNull("createdDateTime")
                .setNotNull("domain").sample();
        Question question = fixtureMonkey.giveMeOne(Question.class);
        given(questionReader.readDaily(user, user.getPreferDomains(), user.getDaysSinceJoined()))
                .willReturn(Optional.of(question));
        given(answerReader.readAllByUserIdAndQuestionId(user.getId(), question.getId()))
                .willReturn(Collections.emptyList());

        // when
        InterviewAccessStatus result = answerService.determineInterviewAccess(user);

        // then
        assertEquals(InterviewAccessStatus.FIRST, result);
    }

    @RepeatedTest(5)
    void 씨앗이_없고_이미_면접을_본_경우_LIMIT_REACHED_반환() {
        // given
        User user = fixtureMonkey.giveMeBuilder(User.class)
                .setNotNull("domain")
                .set("createdDateTime", LocalDateTime.now().minusDays(10L))
                .sample();
        Question question = fixtureMonkey.giveMeOne(Question.class);
        Answer answer = mock(Answer.class);
        given(questionReader.readDaily(user, user.getPreferDomains(), user.getDaysSinceJoined()))
                .willReturn(Optional.of(question));
        given(answerReader.readAllByUserIdAndQuestionId(user.getId(), question.getId()))
                .willReturn(List.of(answer));
        given(creditCounter.countNotUsedCredit(user.getId()))
                .willReturn(0L);

        // when
        InterviewAccessStatus result = answerService.determineInterviewAccess(user);

        // then
        assertEquals(InterviewAccessStatus.LIMIT_REACHED, result);
    }

    @RepeatedTest(5)
    void 씨앗이_있고_이미_면접을_본_경우_SEED_REQUIRED_반환() {
        // given
        User user = fixtureMonkey.giveMeBuilder(User.class)
                .setNotNull("domain")
                .set("createdDateTime", LocalDateTime.now().minusDays(10L))
                .sample();
        Question question = fixtureMonkey.giveMeOne(Question.class);
        Answer answer = mock(Answer.class);
        given(questionReader.readDaily(user, user.getPreferDomains(), user.getDaysSinceJoined()))
                .willReturn(Optional.of(question));
        given(answerReader.readAllByUserIdAndQuestionId(user.getId(), question.getId()))
                .willReturn(List.of(answer));
        given(creditCounter.countNotUsedCredit(user.getId()))
                .willReturn(2L);

        // when
        InterviewAccessStatus result = answerService.determineInterviewAccess(user);

        // then
        assertEquals(InterviewAccessStatus.SEED_REQUIRED, result);
    }

    @RepeatedTest(5)
    void 질문이_없는_경우_예외를_던진다() {
        // given
        User user = fixtureMonkey.giveMeBuilder(User.class)
                .setNotNull("domain")
                .set("createdDateTime", LocalDateTime.now().minusDays(10L))
                .sample();

        given(questionReader.readDaily(user, user.getPreferDomains(), user.getDaysSinceJoined()))
                .willReturn(Optional.empty());

        // when & then
        assertThrows(ApiException.class, () -> answerService.determineInterviewAccess(user));
    }
}
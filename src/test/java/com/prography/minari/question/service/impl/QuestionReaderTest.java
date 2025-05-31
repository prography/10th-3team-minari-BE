package com.prography.minari.question.service.impl;

import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.prography.minari.common.entity.Domain;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.repository.QuestionRepository;
import com.prography.minari.user.entity.PreferDomain;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuestionReader.class)
class QuestionReaderTest {
    private FixtureMonkey fixtureMonkey;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionReader questionReader;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void init() {
        questionRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        fixtureMonkey = FixtureMonkey.builder()
                .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                .build();
    }

    @Test
    void Question엔티티_조회_테스트() {
        // given
        Question sample = fixtureMonkey.giveMeBuilder(Question.class)
                .set("id", null)
                .setNotNull("content")
                .setNotNull("answer")
                .setNotNull("tag")
                .sample();
        Question saved = questionRepository.save(sample);

        // when
        Optional<Question> optionalQuestion = questionReader.read(saved.getId());

        // then
        assertTrue(optionalQuestion.isPresent(), "저장한 질문이 존재하지 않습니다.");

        Question question = optionalQuestion.get(); // orElseThrow로 대체해도 좋음
        assertAll(
                () -> assertEquals(saved.getContent(), question.getContent()),
                () -> assertEquals(saved.getAnswer(), question.getAnswer()),
                () -> assertEquals(saved.getTag(), question.getTag())
        );
    }

    @Test
    @RepeatedTest(5)
    void 데일리_문제조회_테스트() {
        // given
        ArbitraryBuilder<Question> builder = fixtureMonkey.giveMeBuilder(Question.class);

        List<Question> questions = IntStream.range(0, 5)
                .mapToObj(i -> {
                    ArbitraryBuilder<Question> customized = builder
                            .set("createdDateTime", LocalDateTime.now().plusDays(i));
                    return customized.sample();
                })
                .toList();
        questionRepository.saveAll(questions);
        List<Domain> domains = Stream.of(Domain.values())
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            Collections.shuffle(list);
                            return list.subList(0, Math.min(3, list.size())); // 예: 최대 3개
                        }
                ));
        List<PreferDomain> list = domains.stream().map(domain -> {
            return PreferDomain.builder()
                    .name(domain)
                    .build();
        }).toList();

        User user = fixtureMonkey.giveMeBuilder(User.class)
                .set("createdDateTime", LocalDateTime.now())
                .set("preferDomains",list)
                .sample();
        User saveUser = userRepository.save(user);

        // when
        Optional<Question> question = questionReader.readDaily(saveUser, saveUser.getPreferDomains(), user.getDaysSinceJoined());
        // then

        Optional<Question> willFindQuestion = question.stream()
                .filter(q -> user.getPreferDomains().contains(q.getDomain())).min(new Comparator<Question>() {
                    @Override
                    public int compare(Question o1, Question o2) {
                        return Integer.compare(o1.getOrderNum(), o2.getOrderNum());
                    }
                });

        if (willFindQuestion.isPresent()) {
            assertThat(willFindQuestion.get().getOrderNum())
                    .isEqualTo(question.get().getOrderNum());
        } else {
            assertThat(question.isEmpty()).isTrue();
        }
    }
}
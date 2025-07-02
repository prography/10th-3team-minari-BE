package com.prography.minari.payment.service.impl;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.prography.minari.payment.entity.Product;
import com.prography.minari.payment.repository.ProductJpaRepository;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.repository.QuestionRepository;
import com.prography.minari.question.service.impl.QuestionReader;
import com.prography.minari.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(ProductReader.class)
class ProductReaderTest {
    private FixtureMonkey fixtureMonkey;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ProductJpaRepository productJpaRepository;
    @Autowired
    private ProductReader productReader;

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
    void 현재_판매중인_상품조회_테스트() {
        // given
        List<Product> products = new ArrayList<>();

        // 3개: active=true 인 제품
        for (int i = 0; i < 3; i++) {
            Product product = fixtureMonkey.giveMeBuilder(Product.class)
                    .set("active", true)
                    .sample();
            products.add(product);
        }

        // 7개: 랜덤 제품
        for (int i = 0; i < 7; i++) {
            Product product = fixtureMonkey.giveMeBuilder(Product.class)
                    .set("active", false)
                    .sample();
            products.add(product);
        }
        List<Product> saved = productJpaRepository.saveAll(products);

        // when
        List<Product> result = productReader.readAllActive();
        // then
        assertTrue(result.size() == 3);
    }
}
package com.prography.minari.payment.service;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.prography.minari.payment.entity.Product;
import com.prography.minari.payment.service.dto.SellingProductResponseDto;
import com.prography.minari.payment.service.impl.ProductReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductReader productReader;
    @InjectMocks
    private ProductService productService;
    private FixtureMonkey fixtureMonkey;

    @BeforeEach
    void setUp() {
        fixtureMonkey = FixtureMonkey.builder()
                .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                .build();
    }

    @Test
    void active가_활성화된_상품만_조회하여_지급개수순으로_정렬테스트() {
        // given
        List<Product> products = new ArrayList<>();

        // 3개: active=true 인 제품
        for (int i = 3; i >0; i--) {
            Product product = fixtureMonkey.giveMeBuilder(Product.class)
                    .set("active", true)
                    .set("quantity", i)
                    .sample();
            products.add(product);
        }

        when(productReader.readAllActive()).thenReturn(products);
        // when
        List<SellingProductResponseDto> sellingProducts = productService.getSellingProducts();
        // then
        assertAll(
                () -> assertThat(sellingProducts.size()).isEqualTo(3),
                ()->assertThat(sellingProducts).isSortedAccordingTo(Comparator.comparing(SellingProductResponseDto::getQuantity))
        );

    }
}
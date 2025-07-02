package com.prography.minari.payment.service;

import com.prography.minari.payment.entity.Product;
import com.prography.minari.payment.service.impl.ProductReader;
import com.prography.minari.payment.service.dto.SellingProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductReader productReader;

    public List<SellingProductResponseDto> getSellingProducts() {
        List<Product> products = productReader.readAllActive();
        return products.stream()
                .map(
                        p -> {
                            return SellingProductResponseDto.builder()
                                    .realPrice(p.getRealPrice())
                                    .fakePrice(p.getFakePrice())
                                    .message(p.getMessage())
                                    .quantity(p.getQuantity())
                                    .build();
                        }
                ).sorted(Comparator.comparing(SellingProductResponseDto::getQuantity))
                .toList();
    }
}

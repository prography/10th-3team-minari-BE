package com.prography.minari.payment.controller;

import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.payment.controller.docs.ProductApiDocs;
import com.prography.minari.payment.service.CreditHistoryService;
import com.prography.minari.payment.service.ProductService;
import com.prography.minari.payment.service.dto.CreditUsageHistoryResponseDto;
import com.prography.minari.payment.service.dto.SellingProductResponseDto;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController implements ProductApiDocs {
    private final ProductService productService;
    private final CreditHistoryService creditHistoryService;

    @GetMapping("/products/sell")
    public CommonResponse<List<SellingProductResponseDto>> getSellingProducts() {
        List<SellingProductResponseDto> products = productService.getSellingProducts();
        return CommonResponse.success(products);
    }

    @GetMapping("/products/history")
    public CommonResponse<List<CreditUsageHistoryResponseDto>> getSellingProductHistory(@AuthenticationPrincipal User user) {
        return CommonResponse.success(creditHistoryService.getCreditUsageHistory(user.getId()));
    }
}

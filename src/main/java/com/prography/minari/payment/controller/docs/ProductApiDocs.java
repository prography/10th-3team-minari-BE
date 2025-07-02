package com.prography.minari.payment.controller.docs;

import com.prography.minari.common.response.CommonResponse;
import com.prography.minari.payment.service.dto.SellingProductResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(name = "상품 API", description = "상품 관련 API입니다.")
public interface ProductApiDocs {
    @Operation(
            summary = "판매 중인 상품 목록 조회",
            description = "현재 판매 중인 상품 목록을 조회합니다."
    )
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/api/v1/products/sell")
    CommonResponse<List<SellingProductResponseDto>> getSellingProducts();
}

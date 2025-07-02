package com.prography.minari.payment.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SellingProductResponseDto {
    @Schema(description = "씨앗의 개수", example = "1")
    @Column(name = "quantity")
    private Integer quantity;
    @Schema(description = "실제 결제 금액", example = "100")
    @Column(name = "real_price")
    private Long realPrice;
    @Schema(description = "할인 전 금액", example = "200")
    @Column(name = "fake_price")
    private Long fakePrice;
    @Schema(description = "할인에 대한 문구(html로 넘어갈 예정)", example = "")
    @Column(name = "message")
    private String message;
}

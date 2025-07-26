package com.prography.minari.payment.service.dto;

import com.prography.minari.payment.entity.PayCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreditUsageHistoryResponseDto {
    private String date;
    private Long quantity;
    private String category;
    private boolean refund;
    private Long remain;
}

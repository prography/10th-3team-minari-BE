package com.prography.minari.payment.service.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CreditUsageTarget {
    INTERVIEW("면접진행");
    private String description;
}

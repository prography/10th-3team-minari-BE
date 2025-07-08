package com.prography.minari.payment.entity;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class AccountPayment extends Payment {
    private String memo;
    private String reason;
    private String role;

    public AccountPayment(Long userId, Long productId, Long amount,
                          String memo, String reason, String role) {
        super(userId, productId, amount); // 부모 필드 설정
        this.memo = memo;
        this.reason = reason;
        this.role = role;
    }
}

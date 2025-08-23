package com.prography.minari.payment.entity;

import com.prography.minari.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.prography.minari.payment.entity.CreditStatus.PAID;

@Entity
@Table(name = "CREDIT")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Credit extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount; // 씨앗 개수
    private Long userId; // 사용자 번호
    private Long paymentId; // 결제 번호
    private Long productId; // 구매한 상품번호
    @Enumerated(EnumType.STRING)
    private CreditStatus status; // 사용, 환불

    private LocalDateTime expiredDateTime;

    public boolean isRefund() {
        return status.equals(CreditStatus.REFUND);
    }

    public void changeStatusOfExpiredCredit() {
        this.status = CreditStatus.EXPIRED;
    }

    public static Credit create(Long amount, Long userId, Long paymentId, Long productId) {
        Credit credit = new Credit();
        credit.amount = amount;
        credit.userId = userId;
        credit.paymentId = paymentId;
        credit.productId = productId;
        credit.expiredDateTime = LocalDateTime.now().plusYears(1); // 충전된 포인트의 이용기간과 환불가능 기간은 결제시점으로부터 1년 이내로 제한
        return credit;
    }
}

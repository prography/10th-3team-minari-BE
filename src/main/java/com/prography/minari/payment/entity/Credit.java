package com.prography.minari.payment.entity;

import com.prography.minari.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public boolean isRefund(){
        return status.equals(CreditStatus.REFUND);
    }
}

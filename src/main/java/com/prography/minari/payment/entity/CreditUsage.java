package com.prography.minari.payment.entity;

import com.prography.minari.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CREDIT_USAGE")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditUsage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;

    private Long usedAmount;
    private Long usedTarget;//어디에 사용했는지
}

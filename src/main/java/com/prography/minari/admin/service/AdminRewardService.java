package com.prography.minari.admin.service;

import com.prography.minari.payment.entity.AccountPayment;
import com.prography.minari.payment.entity.Credit;
import com.prography.minari.payment.entity.PaymentLog;
import com.prography.minari.payment.entity.Seed;
import com.prography.minari.payment.repository.CreditJpaRepository;
import com.prography.minari.payment.service.impl.CreditWriter;
import com.prography.minari.payment.service.impl.PaymentWriter;
import com.prography.minari.payment.service.impl.SeedReader;
import com.prography.minari.payment.service.impl.SeedWriter;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.impl.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.prography.minari.payment.entity.CreditStatus.PAID;

@Service
@RequiredArgsConstructor
public class AdminRewardService {
    private final UserReader userReader;
    private final SeedReader seedReader;
    private final SeedWriter seedWriter;
    private final PaymentWriter paymentWriter;
    private final CreditJpaRepository creditJpaRepository;
    private final CreditWriter creditWriter;

    public void giveSeedForce(String userUUID, int seeds, String role, String reason, String memo) {
        User user = userReader.readByUUID(userUUID);
        Seed seed = seedReader.readByUserId(user.getId()).orElse(new Seed(0L, user));
        seedWriter.write(seed);
        seed.charge(seeds);
        paymentWriter.write(PaymentLog.builder()
                .userId(user.getId())
                .memo(memo)
                .reason(reason)
                .role(role)
                .build());
    }

    public void giveSeedForceV2(String userUUID, long seeds, String role, String reason, String memo, Long productId) {
        User user = userReader.readByUUID(userUUID);
        if (productId == null) {
            productId = 0L;
        }
        // product id 가 null이면 어드민에서 개수입력 -> 0으로 설정
        // payment 저장 (이력 저장)
        // credit 넣어주기

        /*AccountPayment accountPayment = AccountPayment.builder()
                .userId(user.getId())
                .productId(productId)
                .memo(memo)
                .reason(reason)
                .role(role)
                .amount(seeds)
                .build();
*/
        AccountPayment accountPayment = AccountPayment.create(user.getId(), productId, seeds, memo, reason, role);
        paymentWriter.write(accountPayment);
        creditWriter.write(Credit.builder()
                .paymentId(accountPayment.getId())
                .userId(user.getId())
                .amount(seeds)
                .productId(productId)
                .status(PAID)
                .build());
    }
}

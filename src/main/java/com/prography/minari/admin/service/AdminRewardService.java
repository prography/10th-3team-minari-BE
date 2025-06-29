package com.prography.minari.admin.service;

import com.prography.minari.payment.entity.PaymentLog;
import com.prography.minari.payment.entity.Seed;
import com.prography.minari.payment.service.impl.PaymentWriter;
import com.prography.minari.payment.service.impl.SeedReader;
import com.prography.minari.payment.service.impl.SeedWriter;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.impl.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminRewardService {
    private final UserReader userReader;
    private final SeedReader seedReader;
    private final SeedWriter seedWriter;
    private final PaymentWriter paymentWriter;

    public void giveSeedForce(String userUUID, int seeds, String role, String reason, String memo) {
        User user = userReader.readByUUID(userUUID);
        Seed seed = seedReader.readByUserId(user.getId()).orElse(new Seed(0L,user));
        seedWriter.write(seed);
        seed.charge(seeds);
        paymentWriter.write(PaymentLog.builder()
                .userId(user.getId())
                .memo(memo)
                .reason(reason)
                .role(role)
                .build());
    }
}

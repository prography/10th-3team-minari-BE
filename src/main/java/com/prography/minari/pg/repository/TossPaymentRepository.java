package com.prography.minari.pg.repository;

import com.prography.minari.pg.entity.TossPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import reactor.core.publisher.Mono;

public interface TossPaymentRepository extends JpaRepository<TossPayment, String> {

}

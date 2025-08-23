package com.prography.minari.payment.repository;

import com.prography.minari.payment.entity.PGPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PgPaymentRepository extends JpaRepository<PGPayment, Long> {
}
